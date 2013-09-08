int DigitalWhiteLED = 4;

void setup() {
  pinMode(DigitalWhiteLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 digitalWrite(DigitalWhiteLED, true);
 delay(800);
 digitalWrite(DigitalWhiteLED, false);
 delay(800);
}
