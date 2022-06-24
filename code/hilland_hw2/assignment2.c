#include <stdio.h>
#include <argp.h>
#include <stdbool.h>

const char *argp_program_version = "1.0.0.dev1";

const char *argp_program_bug_address = "jhilland@unm.edu";

struct arguments {
    char *arg;  // for string argument
    char *url;    
    bool post;
    bool get;
    bool put;
    bool delete;
};

static struct argp_option options[] = {
    {"url", 'u', "String", 0, "URL for HTTP Request"},
    {"post", 'o', 0, OPTION_ARG_OPTIONAL, "POST HTTP Request"},
    {"get", 'g', 0, OPTION_ARG_OPTIONAL, "GET HTTP Request"},
    {"put", 'p', 0, OPTION_ARG_OPTIONAL, "GET HTTP Request"},
    {"delete", 'd', 0, OPTION_ARG_OPTIONAL, "GET HTTP Request"},
    {0}
};

static error_t parse_opt(int key, char *arg, struct argp_state *state) {
    struct arguments *arguments = state->input;

    switch (key) {
        case 'u':
            arguments->url = arg;
            printf("url, %s\n", arguments->url);
            break;
        case 'o':
            arguments->post = true;
            printf("post\n");
            break; 
        case 'g':
            arguments->get = true;
            printf("get\n");
            break;
        case ARGP_KEY_ARG:
            if (state->arg_num > 1) {
                printf("Too many arguments, use 'quotes around your extra argument'");
                argp_usage(state);
                break;
            }
            arguments->arg = arg;
            printf("arg, %s\n", arg);
        case ARGP_KEY_END:
            if (arguments->url == NULL) {
                printf("Missing required url, please provide to continue\n");
                argp_usage (state);
            }
            break;
        default:
            return ARGP_ERR_UNKNOWN;
    }
    return 0;
}

static char args_doc[] = "-u http://localhost:8080 -o 'argument to pass'";

static char doc[] = "Provide a url and conduct a get, post, delete or put request.";

static struct argp argp = {options, parse_opt, args_doc, doc};

int main(int argc, char **argv) {
    struct arguments arguments;
    FILE *outstream;

    /* Set argument defaults */
    arguments.url = NULL;
    arguments.post = false;
    arguments.get = false;
    arguments.put = false;
    arguments.delete = false;

    /* Where the magic happens */
    argp_parse(&argp, argc, argv, 0, 0, &arguments);

    outstream = stdout;

    return 0;
}