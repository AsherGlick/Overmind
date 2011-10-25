void charprint (std::string output) {
  for (unsigned int i = 0; i < output.size(); i++) {
    if (output[i] >= 33 && output[i] <= 126) {
      std::cout << output[i];
    }
    else {
      std::cout << '(' << int(output[i]) << ')';
    }
  }
  std::cout << std::endl;
}

std::string asciiOnly (std::string input) {
  std::string output;
  for (unsigned int i = 0; i < input.size(); i++) {
    if (input[i] >= 33 && input[i] <= 126) {
      output += input[i];
    }
  }
  return output;
}
