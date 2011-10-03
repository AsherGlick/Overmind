/* This is a custom program for converting between strings (c++) and 
strings (c)
*/
#include <string>
int freestr(char * str) {
  return 0;//free(str);
}

std::string strtoString(char * str) {
  std::string output;
  //printf("%s\n",str);
  //printf("BEGIN\n");
  while (*str != '\0') {
    //printf("ITER: %c\n",*str);
    output += *str ;
    str++;
  }
  //printf("END\n");
  return output;
}

int stringtoStr(char * & str, std::string cppstr) {
  int size = cppstr.size();
  str = (char *) malloc (sizeof(char)*(size+1));
  for (int i = 0; i < size; i ++){
    str[i] = cppstr[i];
  }
  str[size]='\0';
  return size;
}

char * stringtoStr(std::string str) {
  return 0;
}

