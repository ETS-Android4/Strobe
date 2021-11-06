#include <TimerOne.h>
#include <FastLED.h>

#define NP 64

#define Magnet 5
#define Pump 3

#define LED_max 20001

String string;
String sign;
char command, mark;
unsigned int l;
bool sync;

unsigned char colors[6][3], add_hsv[6]={0}, count_hsv[6]={1}, count[6]={2}, selected = 0, pumpValue, attachValue[8], c[6]={0}, add_color=0, mag=64;
unsigned short latency[6]={100}, onTime[6]={0}, global={1};
bool chosen[6], attachBool[8]= {false};
unsigned int pumpCount, ledCount=0, pot, x=1000;

int off, on;

CRGB leds[NP];
unsigned char startIndex = 0;
unsigned char brightness = 192;
bool LED = true;
bool model = true;
bool magnet = true;
bool pump = true;
bool Sync = false;

unsigned char magValue = 48;

void setup()
{
  chosen[0] = true;
  count[0]=2;
  latency[0]=1600;
  colors[0][0]=0;
  colors[0][1]=0;
  colors[0][2]=255;
 
  delay(1000);
  pinMode(Magnet, OUTPUT);
  pinMode(Pump, OUTPUT);
  
  FastLED.addLeds<APA102, 11, 13, BGR, DATA_RATE_MHZ(12)>(leds, NP);
  FastLED.setBrightness(192);
  
  Serial.begin(9600);
  Serial.setTimeout(200);

  Timer1.initialize(500);
  Timer1.attachInterrupt(pump_thread);
  
  delay(1000);  
}

void pump_thread(void){
  if(pump){
   if(pumpValue){
      if(pumpCount==pumpValue){
       digitalWrite(Pump, HIGH);}
     if(pumpCount>=40){
       digitalWrite(Pump, LOW);
       pumpCount=0;}
       pumpCount++;
    }
    else
      digitalWrite(Pump, HIGH);
  }
  else
    digitalWrite(Pump, LOW);
}

//Bluetooth
void Bluetooth(){
  if (Serial.available() > 0){
      string = "";
      l=0;
      mark = "";}
  while(Serial.available() > 0){
    command = ((byte)Serial.read());
    if(command == 'x')
      break;
    else{
      string += command;}
    delay(1);
  }
  Read();
  /*if(string!=""){
    Serial.print("String: "); Serial.println(string[string.length()-1]);
    Serial.print("Number: "); Serial.println(string.toInt());
  }*/
}

void Read(){
  mark = string[string.length()-1];
  l = string.toInt();
  switch(mark){
    case 'Q': switch(l){
      case 0: LED = true; break;
      case 1: LED = false; break;
      case 2: magnet = true; break;
      case 3: magnet = false; break;
      case 4: pump = true; break;
      case 5: pump = false; break;
      case 6: Sync = true; break;
      case 7: Sync = false; break;
    }
    //menu
    case 'b': brightness = l; FastLED.setBrightness(l); break;
    case 'p': pumpValue=l; break;
    case 'm': magValue=l; break;

    case 'P': x = l; break;
    //Palette
    case 'R': colors[selected][0] = l; break;
    case 'G': colors[selected][1] = l; break;
    case 'B': colors[selected][2] = l; break;
    case 'h': count_hsv[selected] = l; break;
    case 'v': add_hsv[selected] = l; c[selected]=0; break;
    //Home
    case 'S': selected = l; break;
    case 'A': for(int i=1; i<7;i++) {sign = string[i]; chosen[i-1]=sign.toInt();} break;
    case 'c': count[selected] = l; break;
    case 'd': latency[selected] = l; break;
    case 'o': onTime[selected] = l; break;
    case 'g': global = l; break;
    //Home attach
    case 'C': for(int i=1; i<9; i++){
      if(string[i]!='9'){
        attachBool[i-1]=true; sign = string[i]; attachValue[i-1]=sign.toInt();
      }else{attachBool[i-1]=false;}
      }break;
    case 'O': add_color = l; break;
  }
  l=0;
  mark = "";
  string = "";
}

void ledFunction(){
  char i=0, colorBuffer[3]={0}, countBuffer=0, latencyBuffer=0, onBuffer=0, hBuffer=0, aBuffer=0;
  
      if(attachBool[0]){
        countBuffer = attachValue[0];}
      if(attachBool[1]){
        latencyBuffer = attachValue[1];}
      if(attachBool[2]){
        onBuffer = attachValue[2];}
      if(attachBool[3]){
        colorBuffer[0] = 0;}
      if(attachBool[4]){
        colorBuffer[1] = 0;}
      if(attachBool[5]){
        colorBuffer[2] = 0;}
      if(attachBool[6]){
        hBuffer = 0;}
      if(attachBool[7]){
        aBuffer = 0;}
        
  if(LED){
    while(i<6){
      if(ledCount%count[countBuffer]==1){
        if(chosen[i]){
          fill_solid(leds, NP, CHSV(colors[colorBuffer[0]][0]+c[i]+add_color*(i), colors[colorBuffer[1]][1], colors[colorBuffer[2]][2]));
          FastLED.show();
          delayMicroseconds(onTime[onBuffer]*10);
        }else{
          //delayMicroseconds(824);
        }
        FastLED.clear();
        FastLED.show();
        delayMicroseconds(latency[latencyBuffer]*10);
      }
      if(count_hsv[hBuffer]==1||add_hsv[aBuffer]==0){
         c[i]=0;}
      if(ledCount%count_hsv[hBuffer]==1){
        c[i]+=add_hsv[aBuffer];}
      i++;
      
      if(!attachBool[0]){
        countBuffer++;}
      if(!attachBool[1]){
        latencyBuffer++;}
      if(!attachBool[2]){
        onBuffer++;}
      if(!attachBool[3]){
        colorBuffer[0]++;}
      if(!attachBool[4]){
        colorBuffer[1]++;}
      if(!attachBool[5]){
        colorBuffer[2]++;}
      if(!attachBool[6]){
        hBuffer++;}
      if(!attachBool[7]){
        aBuffer++;}
      
      }
  }
  delayMicroseconds(global*10);
  FastLED.clear();
  FastLED.show();
}

void pokaz(){
  switch(x){
    case 0:
      if(brightness<192){
        if(ledCount%10==1){
          brightness+=3;
          FastLED.setBrightness(brightness);} 
      }
      break;
    case 1:
      FastLED.setBrightness(192);
      if(onTime[0]<1300){
        if(ledCount%3==1){
          onTime[0]+=10;
          latency[0]-=10;} 
      }
      break;
    case 2:
      onTime[0] = 0;
      count[0] = 5;
      latency[0]=49;
      add_color=43;
      string = "100000000C";
      Read();
      string = "1111111A";
      Read();
      x=100;
      break;
    case 3:
      if(colors[0][1]<255){
        if(ledCount%4==1){
          colors[0][1]+=1;} 
      }
      break;
    case 4:
      count_hsv[0] = 12;
      add_hsv[0]=8;
      x=100;
      break;
    case 5:
      count[0]=2;
      latency[0]=0;
      if(ledCount%8==1){
        if(onTime[0]<100)
          onTime[0]+=5;
        else{
          if(add_hsv[0]>10)
            add_hsv[0]--;
          if(count_hsv[0]<24)
            count_hsv[0]++;
          if(add_color>15)
            add_color-=1;
        }
      }
      break;
    case 6:
      if(ledCount%4==1){
        if(colors[0][2]>0)
          colors[0][2]-=5;
      }
      break;
    case 7:
      add_hsv[0] = 8;
      onTime[0]=0;
      count[0]=4;
      count_hsv[0] = 20;
      latency[1]=0; latency[0]=774;
      count[1]=4; c[0]=0; c[1]=0;
      add_color = 128;
      string = "109900009C";
      Read();
      string = "1110000A";
      Read();
      x = 8;
      break;
    case 8:
      if(ledCount%4==1){
        if(colors[0][2]<255)
          colors[0][2]+=5;
      }
      break;
  }
}

void wait(){
  startIndex++;
  char colorIndex = startIndex;
  for( int i = 0; i < NP/2; i++) {
        leds[i] = ColorFromPalette(RainbowColors_p, colorIndex, 80, LINEARBLEND);
        leds[NP-i] = ColorFromPalette(RainbowColors_p, colorIndex, 80, LINEARBLEND);
    colorIndex += 3;
    }
  FastLED.show();
  delayMicroseconds(125);
  FastLED.clear();
  FastLED.show();
  delayMicroseconds(3950);
}

void templates(){
  switch(pot/100){
      case 0:
      case 1:
      case 2:
        if(!Sync){
          ledFunction();
        }
        else{
          wait();
        }
        break; 
      case 3:
        FastLED.setBrightness(192);
        fill_solid(leds, NP, CHSV(0, 0, 255));
        FastLED.show();
        FastLED.clear();
        FastLED.show();
        delayMicroseconds(16363);
        delayMicroseconds(2390);
        break;
      case 4:
        for(int i =0; i<11; i++){
          fill_solid(leds, NP, CHSV(0, 0, 255/i));
          FastLED.show();
          FastLED.clear();
          FastLED.show();
        }
        break;
      case 5:
        break;
      case 6:
        wait();
        break;
      case 7:
        off=420; on=1600;
        fill_solid(leds, NP, CRGB(255, 0, 0));
        FastLED.show();
        delayMicroseconds(on);
        FastLED.clear();
        FastLED.show();
        fill_solid(leds, NP, CRGB(255, 55, 0));
        FastLED.show();
        delayMicroseconds(on);
        FastLED.clear();
        FastLED.show();
        fill_solid(leds, NP, CRGB(255, 203, 0));
        FastLED.show();
        delayMicroseconds(on);
        FastLED.clear();
        FastLED.show();
        fill_solid(leds, NP, CRGB(0, 255, 0));
        FastLED.show();
        delayMicroseconds(on);
        FastLED.clear();
        FastLED.show();
        fill_solid(leds, NP, CRGB(0, 0, 255));
        FastLED.show();
        delayMicroseconds(on);
        FastLED.clear();
        FastLED.show();
        fill_solid(leds, NP, CRGB(123, 0, 255));
        FastLED.show();
        delayMicroseconds(on);
        FastLED.clear();
        FastLED.show();
        delayMicroseconds(off);
        break;
      case 8:
        FastLED.setBrightness(192);
        fill_solid(leds, NP, CHSV(0, 0, 255));
        FastLED.show();
        FastLED.clear();
        FastLED.show();
        delayMicroseconds(16363);
        delayMicroseconds(2390);
        break;  
    }
}

void loop(){
  Bluetooth();
  pokaz();
  /*if(pump){
      digitalWrite(Pump, HIGH);}
    else{
      digitalWrite(Pump, LOW);
  }*/
  if(pot!=analogRead(A0)){
    pot = analogRead(A0);
  }
  bool flag;
  if(pot>900){
    FastLED.setBrightness(15);
    fill_gradient_RGB(leds, 32, CRGB(48,48,48), 63, CRGB(170,255,0));
    fill_gradient_RGB(leds, 0, CRGB(170,255,0), 31, CRGB(48,48,48));
    FastLED.show();
    noTone(Magnet);
    mag=64;
  }
  else{
    if(!magnet){
      noTone(Magnet);
      mag=64;
    } else {
      if(mag>48){
        if(ledCount%20==1){
          tone(Magnet, mag);
          mag-=1;} 
      }else
        tone(Magnet, magValue);
    }
    templates();   
  }
  ledCount++;
  if(ledCount>LED_max){
    ledCount=0;}
}
