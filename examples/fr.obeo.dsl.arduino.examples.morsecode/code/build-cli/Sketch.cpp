#include <Arduino.h>
int Buzzer = 12;
int RedLED = 13;
int Pushbutton1 = 8;

void setup() {
  pinMode(Buzzer, OUTPUT);  
  pinMode(RedLED, OUTPUT);  
}

// the loop routine runs over and over again forever:
void loop() {
 digitalWrite(RedLED, (!digitalRead(Pushbutton1)));
 digitalWrite(Buzzer, (!digitalRead(Pushbutton1)));
}
