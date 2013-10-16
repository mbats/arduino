int WhiteLED = 11;
int Infraredsensor = 4;

void setup() {
  pinMode(WhiteLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 digitalWrite(WhiteLED, (digitalRead(Infraredsensor)));
}
