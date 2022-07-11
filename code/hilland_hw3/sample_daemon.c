#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>
#include <syslog.h>
#include <time.h>
#include <errno.h>
#include <assert.h>

#define OK              0
#define ERR_SETSID      1
#define SIGTERM         2
#define SIGHUP          3
#define ERR_FORK        4
#define ERR_CHDIR       5
#define ERR_WTF         9
#define DAEMON_NAME     "Test_daemon"

// comments for myself that would not be left in production code
// make project, make -f make-gcc
// find and kill process
// ps -ef | grep sampled
// kill -9 pid, or killall sampled
// chmod 755 filename
// watch output, less /var/log/syslog, /var/log/messages
// move to /usr/sbin on image

static void _signal_handler(const int signal) {
    switch (signal) {
        case SIGHUP:
            break;
        case SIGTERM:
            syslog(LOG_INFO, "received SIGTERM, exiting.");
            closelog();
            exit(OK);
            break;
        default:
            syslog(LOG_INFO, "received unhandled signal");
            closelog();
            exit(ERR_WTF);
    }
}

void _get_time(void) {
    while (1) {
        time_t now;
        time(&now);
        syslog(LOG_INFO, "Time Now: %s", ctime(&now));
        sleep(1);
    }
    syslog(LOG_INFO, "ending time...");
}

int main() {
    openlog(DAEMON_NAME, LOG_PID | LOG_NDELAY | LOG_NOWAIT, LOG_DAEMON);

    syslog(LOG_INFO, "Daemon sampled");

    // fork will return twice
    // it will return -1 if it failed
    // it will return 0 in child proc if it succeeds
    // returns PID in parent process if it succeeds
    pid_t pid = fork();

    // exit if fork fails
    if (pid < 0) {
        syslog(LOG_ERR, strerror(errno));
        return ERR_FORK;
    }

    // check that parent fork returns pid of child, child
    // fork will return success
    if (pid > 0) {
        return OK;
    }


    // create session, set process group leader
    pid_t sid = setsid();
    if (sid < -1) {
        syslog(LOG_ERR, strerror(errno));
        return ERR_SETSID;
    }

    close(STDIN_FILENO);
    close(STDOUT_FILENO);
    close(STDERR_FILENO);

    umask(S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH);

    // change to root directory for safety
    int dir = chdir("/");
    if (dir < 0) {
        syslog(LOG_ERR, strerror(errno));
        return ERR_CHDIR;
    }

    signal(SIGTERM, _signal_handler);
    signal(SIGHUP, _signal_handler);

    _get_time();

    return ERR_WTF;
}