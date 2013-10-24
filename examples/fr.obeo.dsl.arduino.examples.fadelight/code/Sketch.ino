int brightness;

void setup() {
  brightness=0;
}

// the loop routine runs over and over again forever:
void loop() {
 while (brightness<255)
 {
  brightness=(brightness+1);
  analogWrite(11,255-(brightness));
  delay(8);
 }
 while (brightness>1)
 {
  brightness=(brightness-1);
  analogWrite(11,255-(brightness));
  delay(8);
 }
 delay(200);
}
