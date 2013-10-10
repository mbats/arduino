#include <Arduino.h>
#include <Servo.h>
Servo Microservo;
int RotationsensorV1 = 5;

void setup() {
 Microservo.attach(9);
}

// the loop routine runs over and over again forever:
void loop() {
 Microservo.write(analogRead(RotationsensorV1));
}
