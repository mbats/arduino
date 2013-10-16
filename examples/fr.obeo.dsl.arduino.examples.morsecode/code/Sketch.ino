int BlueLED = 13;
int Buzzer = 1;
int Pushbutton1 = 0;

void setup() {
  pinMode(BlueLED, OUTPUT);  
  pinMode(Buzzer, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 digitalWrite(BlueLED, (digitalRead(Pushbutton1)));
 digitalWrite(Buzzer, (digitalRead(Pushbutton1)));
}
