#include <Arduino.h>
#include <Servo.h>
int BlueLED = 13;
int WhiteLED = 11;
int RedLED = 12;
int Fan = 7;
Servo Microservo1;
Servo Microservo2;
int Pushbutton1 = 0;
int Infraredsensor = 4;
int Soundsensor = 1;
int iter_2;
int iter_28;

void setup() {
  pinMode(BlueLED, OUTPUT);  
  pinMode(WhiteLED, OUTPUT);  
  pinMode(RedLED, OUTPUT);  
  pinMode(Fan, OUTPUT);  
 Microservo1.attach(9);
 Microservo2.attach(8);
}

// the loop routine runs over and over again forever:
void loop() {
 if (!digitalRead(Pushbutton1))
 {
  for (iter_2=0; iter_2 < ( 5 ); ++iter_2 )
  {
   digitalWrite(BlueLED, (!HIGH));
   digitalWrite(WhiteLED, (!LOW));
   digitalWrite(RedLED, (!LOW));
   delay(200);
   digitalWrite(BlueLED, (!LOW));
   digitalWrite(WhiteLED, (!HIGH));
   delay(200);
   digitalWrite(WhiteLED, (!LOW));
   digitalWrite(RedLED, (!HIGH));
   delay(200);
   digitalWrite(BlueLED, (!LOW));
   digitalWrite(WhiteLED, (!LOW));
   digitalWrite(RedLED, (!LOW));
   delay(200);
  }
 }
 if (!digitalRead(Infraredsensor))
 {
  Microservo1.write(90);
  delay(400);
  Microservo1.write(0);
  delay(400);
 }
 if (analogRead(Soundsensor)>128)
 {
  digitalWrite(BlueLED, (!HIGH));
  for (iter_28=0; iter_28 < ( 3 ); ++iter_28 )
  {
   Microservo2.write(0);
   delay(50);
   digitalWrite(Fan, (!HIGH));
   delay(1500);
   digitalWrite(Fan, (!LOW));
   Microservo2.write(60);
   delay(1500);
  }
 }
 digitalWrite(BlueLED, (!LOW));
}
