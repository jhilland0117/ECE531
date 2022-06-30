#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>
#include <syslog.h>

#define OK              0
#define ERR_SETSID      1
#define SIGTERM         2
#define SIGHUP          3
#define ERR_FORK        4
#define ERR_CHDIR       5
#define ERR_WTF         9
#define DAEMON_NAME     "Test_daemon"

// find and kill process
// ps -ef | grep test
// kill -9 pid

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
    }
}

void _do_work(void) {
    for (int i = 0; i < 10000; i++) {
        syslog(LOG_INFO, "iteration: %d", i);
        sleep(1);
    }
}

int main() {
    openlog(DAEMON_NAME, LOG_PID | LOG_NDELAY | LOG_NOWAIT, LOG_DAEMON);

    syslog(LOG_INFO, "Daemon sampled");

    pid_t pid = fork();
    // check to see if fork was successful
    if (pid < 0) {
        syslog(LOG_ERR, strerror(pid));
        return ERR_FORK;
    }

    if (pid > 0) {
        return OK;
    }

    if (setsid() < -1) {
        syslog(LOG_ERR, strerror(pid));
        return ERR_SETSID;
    }

    close(STDIN_FILENO);
    close(STDOUT_FILENO);
    close(STDERR_FILENO);

    umask(S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH);

    if (chdir("/") < 0) {
        syslog(LOG_ERR, strerror(pid));
        return ERR_CHDIR;
    }

    signal(SIGTERM, _signal_handler);
    signal(SIGHUP, _signal_handler);

    _do_work();

    return ERR_WTF;
}