#include <Arduino.h>
#include <Servo.h>
Servo Microservo1;
int RotationsensorV1 = 5;

void setup() {
 Microservo1.attach(9);
}

// the loop routine runs over and over again forever:
void loop() {
 Microservo1.write(analogRead(RotationsensorV1));
}
