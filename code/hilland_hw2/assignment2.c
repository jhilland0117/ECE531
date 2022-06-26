#include <stdio.h>
#include <argp.h>
#include <stdbool.h>
#include <curl/curl.h>
#include <string.h>

#define NO_ARG      0
#define OK          0
#define INIT_ERR    1
#define REQ_ERR     2

/* Hilland, Joseph ECE 531 Homework assignment 2
 * Takes command line options and conducts http request based on parameters.
 * Code is not in production state, improvements can be made by consolidating code.
 */

const char *argp_program_version = "1.0.0.dev1";
const char *argp_program_bug_address = "jhilland@unm.edu";

// arguments will be used for storing values from command line
struct Arguments {
    char *arg;  // for string argument
    char *url;    
    bool post;
    bool get;
    bool put;
    bool delete;
};

// argp options required for output to user
static struct argp_option options[] = {
    {"url", 'u', "String", NO_ARG, "URL for HTTP Request, REQUIRED"},
    {"post", 'o', NO_ARG, NO_ARG, "POST HTTP Request, requires VERB"},
    {"get", 'g', NO_ARG, NO_ARG, "GET HTTP Request"},
    {"put", 'p', NO_ARG, NO_ARG, "GET HTTP Request, requires VERB"},
    {"delete", 'd', NO_ARG, NO_ARG, "GET HTTP Request, requires VERB"},
    {0}
};

// http get, no message required per this assignment
static int send_get_request(char *url) {
    printf("sending get request at url: %s", url);
    CURL *curl = curl_easy_init();
    if (curl) {
        CURLcode res;
        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);
        res = curl_easy_perform(curl);

        if (res != CURLE_OK) {
            return REQ_ERR;
        }

        curl_easy_cleanup(curl);
    } else {
        return INIT_ERR;
    }
    return OK;
}

// http post, message provided as post parameters
static int send_post_request(char *url, char *message) {
    printf("sending post request at url: %s and message: %s\n", url, message);
    CURL *curl = curl_easy_init();
    if (curl) {
        CURLcode res;
        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, message);
        res = curl_easy_perform(curl);

        if (res != CURLE_OK) {
            return REQ_ERR;
        }

        curl_easy_cleanup(curl);
    } else {
        return INIT_ERR;
    }
    return OK;
}

// http put, message provided as put parameters
static int send_put_request(char *url, char *message) {
    printf("sending put request at url: %s and message: %s\n", url, message);
    CURL *curl = curl_easy_init();
    if (curl) {
        CURLcode res;
        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, "PUT");
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, message);
        res = curl_easy_perform(curl);

        if (res != CURLE_OK) {
            return REQ_ERR;
        }

        curl_easy_cleanup(curl);
    } else {
        return INIT_ERR;
    }
    return OK;
}

// http delete, message provided as parameters
static int send_delete_request(char *url, char *message) {
    printf("sending delete request at url: %s and message: %s\n", url, message);
    CURL *curl = curl_easy_init();
    if (curl) {
        CURLcode res;
        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, "DELETE");
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, message);
        res = curl_easy_perform(curl);

        if (res != CURLE_OK) {
            return REQ_ERR;
        }

        curl_easy_cleanup(curl);
    } else {
        return INIT_ERR;
    }
    return OK;
}

// function to parse commandline options and arguments
static error_t parse_opt(int key, char *arg, struct argp_state *state) {
    struct Arguments *arguments = state->input;

    switch (key) {
        case 'u':
            arguments->url = arg;
            break;
        case 'o':
            arguments->post = true;
            break; 
        case 'g':
            arguments->get = true;
            break;
        case 'p':
            arguments->put = true;
            break;
        case 'd':
            arguments->delete = true;
            break;
        case ARGP_KEY_NO_ARGS:
            // if no arguments are detected, check if verb is required
            if (arguments->post == true || arguments->put == true || arguments->delete == true) {
                printf("You need to supply a VERB.\n");
                argp_usage(state);
                return REQ_ERR;
            }
        case ARGP_KEY_ARG:
            if (state->arg_num > 1) {
                printf("Too many arguments, use quotes around your extra argument.\n");
                argp_usage(state);
                return REQ_ERR;
            }
            arguments->arg = arg;
            break;
        case ARGP_KEY_END:
            if (arguments->url == NULL) {
                printf("Please provide a valid urnl.\n");
                argp_usage(state);
                return REQ_ERR;
            }
            break;
        case ARGP_KEY_SUCCESS:
            if (arguments->get) {
                int err = send_get_request(arguments->url);
                break;
            } else if (arguments->post) {
                int err = send_post_request(arguments->url, arguments->arg);
                break;
            } else if (arguments->put) {
                int err = send_put_request(arguments->url, arguments->arg);
                break;
            } else if (arguments->delete) {
                int err = send_delete_request(arguments->url, arguments->arg);
                break;
            }
            break;
        default:
            return ARGP_ERR_UNKNOWN;
    }
    return 0;
}

static char args_doc[] = "-u http://localhost:8000 -o 'argument to pass'";

static char doc[] = "Provide a url and conduct a get, post, delete or put request.";

static struct argp argp = {options, parse_opt, args_doc, doc};

int main(int argc, char **argv) {
    struct Arguments arguments;

    // default arguments, which could be done in struct
    arguments.url = NULL;
    arguments.arg = NULL;
    arguments.post = false;
    arguments.get = false;
    arguments.put = false;
    arguments.delete = false;

    // parse the arguments
    argp_parse(&argp, argc, argv, 0, 0, &arguments);

    return 0;
}