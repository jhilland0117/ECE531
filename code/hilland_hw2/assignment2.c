#include <stdio.h>
#include <argp.h>
#include <stdbool.h>

const char *argp_program_version = "1.0.0.dev1";

const char *argp_program_bug_address = "jhilland@unm.edu";

/* This structure is used by main to communicate with parse_opt. */
struct arguments {
    char *url;
    bool post;
    bool get;
};

/*
OPTIONS.  Field 1 in ARGP.
Order of fields: {NAME, KEY, ARG, FLAGS, DOC}.
*/
static struct argp_option options[] = {
    {"url", 'u', "http://localhost:8000", 0, "URL for HTTP Request"},
    {"post", 'o', 0, 0, "POST HTTP Request"},
    {"get", 'g', 0, 0, "GET HTTP Request"},
    {0}
};

/*
PARSER. Field 2 in ARGP.
Order of parameters: KEY, ARG, STATE.
*/
static error_t parse_opt(int key, char *arg, struct argp_state *state) {
    struct arguments *arguments = state->input;

    switch (key) {
        case 'u':
            arguments->url = arg;
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
            break;
        default:
            return ARGP_ERR_UNKNOWN;
    }
    return 0;
}

/*
ARGS_DOC. Field 3 in ARGP.
A description of the non-option command-line arguments
    that we accept.
*/
static char args_doc[] = "ARG1 ARG2";

/*
DOC.  Field 4 in ARGP.
Program documentation.
*/
static char doc[] = "Test ";

/*
The ARGP structure itself.
*/
static struct argp argp = {options, parse_opt, args_doc, doc};

/*
The main function.
Notice how now the only function call needed to process
all command-line options and arguments nicely
is argp_parse.
*/
int main(int argc, char **argv) {
    struct arguments arguments;
    FILE *outstream;

    /* Set argument defaults */
    arguments.url = "";
    arguments.post = false;
    arguments.get = false;

    /* Where the magic happens */
    argp_parse(&argp, argc, argv, 0, 0, &arguments);

    outstream = stdout;

    /* Print argument values */
    fprintf(outstream, "url = %s\n", arguments.url);

    return 0;
}