# ECE 531 Spring 2022 Assignment 2

Small C program that implements get, post, delete and put HTTP requests via curl api.
Code uses argp api.
Code comes with two make files, one for standard gcc, the other for cross compiling.
Run the following for linux based distro such as ubuntu:

```
make -f make-gcc
```

To run on ARM, make sure you have the proper packages and libraries installed such as curl and argp:

```
make -f make-arm
```

Finallly you can clean the project by running either:

```
make clean if make-arm or make clean -f make-gcc
```

This code was developed as a homework assignment, currently urls are not parsed and checked via regex as that was not an initial requirement.

Requirements this code should satisfy:

For this assignment, you'll need to write a program that uses libcurl to communicate via HTTP. I'd like the program to take some command line arguments, and execute HTTP GET, POST, PUT, and DELETE. the program needs to support the following command line options:

-u/--url

-o/--post

-g/--get

-p/--put

-d/--delete

-h/--help

The final argument will be an arbitrary string. Be careful with your string processing! You'll need to handle oddly formatted strings with unusual characters. The -u/--url flag will need to handle a properly formatted URL (including localhost) followed by a port number. You should be able to have the command line options in any order.

Your program should return any content returned from the request preceded by the HTTP code (404, 200, etc.).

So, if your program, when compiled, is named 'hw', these are some examples of valid invocations:

hw --post --url http://localhost:8080 here is the message I'm posting to the URL!

hw --get --url http://www.cnn.com

hw --put --url http://localhost:8080 put this stuff at the URL

hw --delete --url http://localhost:8080 78392

If I don't submit a string for a verb that requires one, you need to set the exit status according to UNIX standards (e.g. return 0 if everything is OK, some other value otherwise). I'll be running this program on the ARM emulator we configured, so make sure you test it there.
