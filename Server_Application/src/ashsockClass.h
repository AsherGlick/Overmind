/*
| Licenced under the BSD (Open Licence)
| this is a port for c (not much change) for basic low level socket control
| to be used in a more convienient C++ 
*/
/********************************* FUNCTIONS **********************************\
| sigchl_handler(int s)                                                        |
| void *get_in_addr(struct sockaddr *sa)                                       |
| std::string waitData (int & clientSockFD)                                    |
| bool sendData (int & clientSockFD, std::string output)                       |
| void waitClient(int & clientSockFD, int & sockFD)                            |
| int waitSelf(int & clientSockFD, int & sockFD)      [Soon to be depricated]  |
| void bindPort (int & sockfd, std::string port)                               |
\******************************************************************************/
#ifndef _ASHSOCK_CLASS_H_
#define _ASHSOCK_CLASS_H_
#include <stdio.h> // input out put
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/wait.h>
#include <signal.h>
#include <fcntl.h> // non blocking socket

#include <string>

#include "cstrstr.h"


//#ifndef MAXDATASIZE
//  #define MAXDATASIZE 8000
//#endif

#ifndef BACKLOG
  #define BACKLOG 10
#endif

class socketLink {
  private:
    int _fd; // file descriptor
    bool _open; // true if socket is connected, false if not
    std::string _ipAddress;
    std::string _port;
  public:
    socketLink();                                                // null inherit
    ~socket();
    void close();
    std::string waitData();
    bool connect(std::string ipAddress, std::);
    bool inherit(int fd, std::string ipAddress, std::string port);
};

socketLink::socketLink()
{
  _fd = -1;
  _open = false;
  _ipAddress = "0.0.0.0";
  _port = "-1";
}


/*\
| This function needs to be re-written, to also test the connection. if the
| connection is closed then _open is set to false and the function returns false
\*/
bool socketLink::inherit(int fd, std::string ipAddress, std::string port)
{
  _fd = fd;
  _ipAddress = ipAddress;
  _port = port;
  _open = true;
  return _open;
}








class socketPort {
  private:
    int _fd; // file descriptor
    bool _open; // true if socket is connected, false if not
    std::string _ipAddress;
    std::string _port;
  public:
    socket(std::string port);
    socket(std::string ipAddress, std::string port);
    ~socket();
    void close();
}


void sigchld_handler(int s)
{
    while(waitpid(-1, NULL, WNOHANG) > 0);
}


// get sockaddr, IPv4 or IPv6:
void *get_in_addr(struct sockaddr *sa)
{
    if (sa->sa_family == AF_INET) {
        return &(((struct sockaddr_in*)sa)->sin_addr);
    }
    return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

/********************************** Wait Data *********************************\
| wait for data from a client socket file descriptor. The function return the
| size of the data return in an allocated character array that must be freed
\******************************************************************************/
std::string socketLink::waitData (){
  char * data;
  char buf[MAXDATASIZE];
  char * tempData;
  int numbytes; // number of bytes received
  int totalSize = 0;
  data = (char *)malloc (sizeof(char)*0);
  int i = 0;
  std::string output = "";
  while (1) {
    for (i = 0; i < MAXDATASIZE; i++) {
      buf[i] = 'Z';
    }
    if ((numbytes = recv(&_sockFD, buf, MAXDATASIZE-1, 0)) == -1) {
      // no data waiting
      if (totalSize == 0) {
        free (data);
        return "";
      }
      else {
        break;
      }
    }
    if (numbytes == 0){
      //if a null byte is transfered the socket is closed
      printf ("Socket Closed, zero byte\n");
      //remove socket from list
      close(&_fd);
      _open = false;
      break;
    }
    // ALL HAS GONE WELL, what to do with the information here

    totalSize += numbytes;
    tempData = (char *) malloc (sizeof(char) * (totalSize+1));
    //printf("test mark 2\n");
    
    for (i = 0; i < totalSize-numbytes;i++) {
      tempData[i] = data[i];
    }
    for (;i < totalSize; i++) {
      tempData[i] = buf[i-(totalSize-numbytes)];
    }
    tempData[totalSize] = '\0';
    free (data);
    data = tempData;
  }
  output = strtoString(data);
  free (data);
  return output;
}

/********************************** Send Data *********************************\
| This function sends out data and returns on sucess or failure
\******************************************************************************/
bool sendData (int & clientSockFD, std::string output){
  int size = output.size();
  int sentData = 0;
  while (sentData < size) {
    size -= sentData;
    output = output.substr(sentData,size);
    sentData = send(clientSockFD, output.c_str(), size, 0);
    if (sentData <= -1) {
      sentData = 0;
      continue;
    }
  }
  if (size-sentData < 0) printf("NEGBYTES%i\n",(size-sentData));
  return true;
}


/********************************* Wait Client ********************************\
| Wait client waits for a client to connect to the server and returns a sockFD |
| that connects to the client. This file descripter can be used in waitData    |
\******************************************************************************/
void tcp_socket::waitClient(int & clientSockFD, int & sockFD){
  socklen_t sin_size;
  struct sockaddr_storage their_addr;
  char s[INET6_ADDRSTRLEN];
  
  while(1) {
    sin_size = sizeof their_addr;
    clientSockFD = accept(sockFD, (struct sockaddr *)&their_addr, &sin_size);
    if (clientSockFD == -1) {
      perror("accept");
      continue;
    }
    inet_ntop(their_addr.ss_family,get_in_addr((struct sockaddr *)&their_addr),s, sizeof s);
    //printf("server: got connection from %s\n", s);
    
    fcntl(clientSockFD,F_SETFL,O_NONBLOCK);
    
    break;
  }
}


/********************************** Wait Self *********************************\
| Only lets itself connect to the socket (or 192.168.x.x) soon to be depricated|
| when the IP data is stored in the class                                      |
\******************************************************************************/
int waitSelf(int & clientSockFD, int & sockFD){
  socklen_t sin_size;
  struct sockaddr_storage their_addr;
  char s[INET6_ADDRSTRLEN];
  
  while(1) {
    sin_size = sizeof their_addr;
    clientSockFD = accept(sockFD, (struct sockaddr *)&their_addr, &sin_size);
    if (clientSockFD == -1) {
      perror("accept");
      continue;
    }
    inet_ntop(their_addr.ss_family,get_in_addr((struct sockaddr *)&their_addr),s, sizeof s);
    //printf("server: got connection from %s\n", s);
    if (std::string(s) != "127.0.0.1" && std::string(s).substr(0,7) != "192.168") {
      close(clientSockFD);
      return -1;
    }
    
    fcntl(clientSockFD,F_SETFL,O_NONBLOCK);
    
    return 0;
  }
}




/********************************** Bind port *********************************\
| the bind port function binds itself to a specified port on the computer and  |
| returns the file descriptor of the socket (UNIX-like os)                     |
\******************************************************************************/
void tcp_socket::bindPort (std::string port) {
    struct addrinfo hints, *servinfo, *p;
    struct sigaction sa;
    int yes=1;
    int rv;
    
    
    memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // use my IP
    if ((rv = getaddrinfo(NULL, port.c_str(), &hints, &servinfo)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        exit(1);
    }
    // loop through all the results and bind to the first we can
    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((&_fd = socket(p->ai_family, p->ai_socktype,
                p->ai_protocol)) == -1) {
            perror("server: socket");
            continue;
        }
        if (setsockopt(&_fd, SOL_SOCKET, SO_REUSEADDR, &yes,
                sizeof(int)) == -1) {
            perror("setsockopt");
            exit(1);
        }
        if (bind(&_fd, p->ai_addr, p->ai_addrlen) == -1) {
            close(&_fd);
            perror("server: bind");
            continue;
        }
        break;
    }
    if (p == NULL) {
        fprintf(stderr, "server: failed to bind\n");
        exit(2);
  }
  freeaddrinfo(servinfo); // all done with this structure
  if (listen(&_fd, BACKLOG) == -1) {
      perror("listen");
      exit(1);
  }
  sa.sa_handler = sigchld_handler; // reap all dead processes
  sigemptyset(&sa.sa_mask);
  sa.sa_flags = SA_RESTART;
  if (sigaction(SIGCHLD, &sa, NULL) == -1) {
      perror("sigaction");
      exit(1);
  }
}
#endif
