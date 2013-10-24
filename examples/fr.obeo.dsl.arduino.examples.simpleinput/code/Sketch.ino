int BlueLED = 13;
int Pushbutton1 = 0;

void setup() {
  pinMode(BlueLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 digitalWrite(BlueLED, (digitalRead(Pushbutton1)));
}
