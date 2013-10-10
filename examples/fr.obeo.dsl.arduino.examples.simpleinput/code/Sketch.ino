int WhiteLED = 12;
int Pushbutton1 = 8;

void setup() {
  pinMode(WhiteLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 digitalWrite(WhiteLED, (!digitalRead(Pushbutton1)));
}
