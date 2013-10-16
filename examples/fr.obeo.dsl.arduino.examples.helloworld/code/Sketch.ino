int BlueLED = 13;

void setup() {
  pinMode(BlueLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 delay(300);
 digitalWrite(BlueLED, (HIGH));
 delay(300);
 digitalWrite(BlueLED, (LOW));
}
