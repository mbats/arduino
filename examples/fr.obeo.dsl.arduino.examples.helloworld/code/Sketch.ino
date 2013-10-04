int WhiteLED = 13;

void setup() {
  pinMode(WhiteLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 digitalWrite(WhiteLED, (true));
 delay(500);
 digitalWrite(WhiteLED, (false));
 delay(500);
}
