int brightness;

void setup() {
  brightness=0;
}

// the loop routine runs over and over again forever:
void loop() {
 ** While1 = fr.obeo.dsl.arduino.impl.WhileImpl@167a912
 while (brightness<255)
 {
  brightness=(brightness+1);
  analogWrite(11,255-brightness);
  delay(5);
 }
 ** While2 = 
 analogWrite(11,255-brightness);
 delay(5);
 ** While3 =
}
