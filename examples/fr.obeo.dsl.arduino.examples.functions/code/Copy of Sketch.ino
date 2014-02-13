int BlueLED = 13;
int RedLED = 11;

void setup() {
  pinMode(BlueLED, OUTPUT);  
  pinMode(RedLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 LEDBlinker(100, BlueLED, HIGH, LOW);
 LEDBlinker(300, RedLED, HIGH, RedLED, LOW);
}

void LEDBlinker(int param1, int param2, int param3, int param4, int param5){
delay(param1);
digitalWrite(param2, (param3));
delay(param1);
digitalWrite(param4, (param5));
}
