#include <Arduino.h>
#include <Servo.h>
#include <Servo.h>
Servo Microservo1;
int Infraredsensor = 4;

void setup() {
 Microservo1.attach(9);
}

// the loop routine runs over and over again forever:
void loop() {
 if (!digitalRead(Infraredsensor))
 {
  Microservo1.write(90);
  delay(400);
  Microservo1.write(0);
  delay(400);
 }
}
