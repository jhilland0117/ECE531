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
static char args_doc[] = "-u http://localhost:8000 -o 'argument to pass'";
static char doc[] = "Provide a url and conduct a get, post, delete or put request.";

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
    {NO_ARG}
};

static int send_http_requeset(char *url, char *message, char *type, bool verb) {
    printf("sending %s request at url: %s\n", type, url);
    CURL *curl = curl_easy_init();
    if (curl) {
        CURLcode res;
        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, type);
        if (verb) {
            printf("sending message: %s\n", message);
            curl_easy_setopt(curl, CURLOPT_POSTFIELDS, message);
        } else {
            curl_easy_setopt(curl, CURLOPT_FOLLOWLOCATION, 1L);
        }
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

int handle_requirement_error(char* message, struct argp_state *state) {
    printf(message);
    argp_usage(state);
    return REQ_ERR;
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
            // check if args are required based on request type, notify user
            if (arguments->post == true || arguments->put == true || arguments->delete == true) {
                return handle_requirement_error("You need to supply a VERB.\n", state);
            }
        case ARGP_KEY_ARG:
            // if too many arguments are given, notify user
            if (state->arg_num > 1) {
                printf("Too many arguments, use quotes around your extra argument.\n");
                argp_usage(state);
                return REQ_ERR;
            }
            arguments->arg = arg;
            break;
        case ARGP_KEY_END:
            // if url is null or malformed, notify user
            if (arguments->url == NULL) {
                printf("Please provide a valid url.\n");
                argp_usage(state);
                return REQ_ERR;
            } else if (arguments->get == false && arguments->post == false && arguments->put == false && arguments->delete == false) {
                return handle_requirement_error("You must select http request type.\n", state);
            }
            break;
        case ARGP_KEY_SUCCESS:
            // perform request based on type, should this be limited to only one type allowed...
            if (arguments->get) {
                int err = send_http_requeset(arguments->url, NULL, "GET", false);
                break;
            } else if (arguments->post) {
                int err = send_http_requeset(arguments->url, arguments->arg, "POST", true);
                break;
            } else if (arguments->put) {
                int err = send_http_requeset(arguments->url, arguments->arg, "PUT", true);
                break;
            } else if (arguments->delete) {
                int err = send_http_requeset(arguments->url, arguments->arg, "DELETE", true);
                break;
            }
            break;
        default:
            return ARGP_ERR_UNKNOWN;
    }
    return 0;
}

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