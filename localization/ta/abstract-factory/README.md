---
title: Abstract Factory
category: Creational
language: ta
tag:
 - Gang of Four
---

## இதுவும் அறியப்படுகிறது

கிட்

## நோக்கம்

தொடர்புடைய அல்லது சார்ந்த குடும்பங்களை உருவாக்க ஒரு இடைமுகத்தை வழங்குகிறது
அவற்றின் குறிப்பிட்ட வகுப்புகளைக் குறிப்பிடாமல் பொருள்கள்.

## விளக்கம்

உண்மை உலக உதாரணம்

> ஒரு ராஜ்யத்தை உருவாக்க, பொதுவான தீம் கொண்ட பொருட்கள் தேவை. எல்வன் ராஜ்யத்திற்கு எல்வன் ராஜா, எல்வன் கோட்டை மற்றும் எல்வன் படை தேவை, ஆனால் ஓர்கிஷ் ராஜ்யத்திற்கு ஓர்கிஷ் ராஜா, ஓர்கிஷ் கோட்டை மற்றும் ஓர்கிஷ் படை தேவை. ராஜ்யத்தில் உள்ள பொருட்களுக்கு இடையே ஒரு சார்பு உள்ளது.

எளிய வார்த்தைகளில்

> தொழிற்சாலைகளின் தொழிற்சாலை; தனிப்பட்ட ஆனால் தொடர்புடைய/சார்ந்த தொழிற்சாலைகளை அவற்றின் குறிப்பிட்ட வகுப்புகளைக் குறிப்பிடாமல் ஒன்றாக குழுவாக்கும் ஒரு தொழிற்சாலை.

விக்கிபீடியா கூறுகிறது

> அமூர்த்த தொழிற்சாலை முறை தனிப்பட்ட தொழிற்சாலைகளின் ஒரு குழுவை உள்ளடக்கும் ஒரு வழியை வழங்குகிறது, அவற்றின் குறிப்பிட்ட வகுப்புகளைக் குறிப்பிடாமல் ஒரு பொதுவான தீம் உள்ளது.

**நிரலாக்க உதாரணம்**

மேலே உள்ள ராஜ்ய உதாரணத்தை மொழிபெயர்த்தல். முதலில், நமக்கு பொருட்களுக்கான சில இடைமுகங்கள் மற்றும் செயல்படுத்தல்கள் உள்ளன
ராஜ்யம்.

```java
public interface Castle {
  String getDescription();
}

public interface King {
  String getDescription();
}

public interface Army {
  String getDescription();
}

// Elven implementations ->
public class ElfCastle implements Castle {
  static final String DESCRIPTION = "This is the elven castle!";
  @Override
  public String getDescription() {
    return DESCRIPTION;
  }
}
public class ElfKing implements King {
  static final String DESCRIPTION = "This is the elven king!";
  @Override
  public String getDescription() {
    return DESCRIPTION;
  }
}
public class ElfArmy implements Army {
  static final String DESCRIPTION = "This is the elven Army!";
  @Override
  public String getDescription() {
    return DESCRIPTION;
  }
}

// Orcish implementations similarly -> ...

```

பின்னர் நமக்கு ராஜ்ய தொழிற்சாலைக்கான அமூர்த்தம் மற்றும் செயல்படுத்தல்கள் உள்ளன.

```java
public interface KingdomFactory {
  Castle createCastle();
  King createKing();
  Army createArmy();
}

public class ElfKingdomFactory implements KingdomFactory {

  @Override
  public Castle createCastle() {
    return new ElfCastle();
  }

  @Override
  public King createKing() {
    return new ElfKing();
  }

  @Override
  public Army createArmy() {
    return new ElfArmy();
  }
}

public class OrcKingdomFactory implements KingdomFactory {

  @Override
  public Castle createCastle() {
    return new OrcCastle();
  }

  @Override
  public King createKing() {
    return new OrcKing();
  }

  @Override
  public Army createArmy() {
    return new OrcArmy();
  }
}
```

இப்போது நாம் FactoryMaker ஐப் பயன்படுத்தி தேவையான தொழிற்சாலையைப் பெறலாம், இது தொழிற்சாலைகளை உருவாக்க ஒரு வழியை வழங்குகிறது.

```java
public static class FactoryMaker {

  public enum KingdomType {
    ELF, ORC
  }

  public static KingdomFactory makeFactory(KingdomType type) {
    switch (type) {
      case ELF:
        return new ElfKingdomFactory();
      case ORC:
        return new OrcKingdomFactory();
      default:
        throw new IllegalArgumentException("KingdomType not supported.");
    }
  }
}
```

இப்போது நாம் முழு செயல்முறையை காட்டும் ஒரு முழு உதாரணத்தைப் பார்ப்போம்:

```java
public class App {
  private King king;
  private Castle castle;
  private Army army;

  public void createKingdom(KingdomFactory factory) {
    king = factory.createKing();
    castle = factory.createCastle();
    army = factory.createArmy();
  }
  
  public King getKing() {
    return king;
  }
  
  public Castle getCastle() {
    return castle;
  }
  
  public Army getArmy() {
    return army;
  }

  public static void main(String[] args) {
    App app = new App();
    
    System.out.println("Elf Kingdom");
    app.createKingdom(FactoryMaker.makeFactory(FactoryMaker.KingdomType.ELF));
    System.out.println(app.getArmy().getDescription());
    System.out.println(app.getCastle().getDescription());
    System.out.println(app.getKing().getDescription());
    
    System.out.println("Orc Kingdom");
    app.createKingdom(FactoryMaker.makeFactory(FactoryMaker.KingdomType.ORC));
    System.out.println(app.getArmy().getDescription());
    System.out.println(app.getCastle().getDescription());
    System.out.println(app.getKing().getDescription());
  }
}
```

வெளியீடு:

```
Elf Kingdom
This is the elven Army!
This is the elven castle!
This is the elven king!
Orc Kingdom
This is the orcish Army!
This is the orcish castle!
This is the orcish king!
```

## பயன்பாடு

* ஒரு அமைப்பு பொருட்களின் குடும்பங்களுடன் தொடர்புடையதாக இருக்க வேண்டும், ஆனால் அவற்றின் குறிப்பிட்ட வகுப்புகளைச் சார்ந்திருக்கக்கூடாது
* ஒரு அமைப்பு பல பொருள் குடும்பங்களில் ஒன்றுடன் கட்டமைக்கப்பட்டிருக்க வேண்டும்
* ஒரு குடும்பத்தின் தொடர்புடைய பொருள்களின் கிளாஸ் நூலகம் பயன்படுத்தப்பட வேண்டும், ஆனால் அவற்றின் செயல்படுத்தல்களை வெளிப்படுத்த விரும்பவில்லை

## நன்மைகள்

* ஒரு குறிப்பிட்ட செயல்படுத்தலுக்கான கிளையன்ட் குறியீட்டை தனிமைப்படுத்துகிறது
* ஒரு குடும்பத்தில் உள்ள பொருட்களை ஒன்றாக பயன்படுத்துவதை எளிதாக்குகிறது
* பொருட்களின் குடும்பங்களை மாற்றுவதை ஆதரிக்கிறது

## குறைபாடுகள்

* புதிய பொருள் வகைகளை அறிமுகப்படுத்துவது கடினம்

## தெரிந்த பயன்பாடுகள்

* javax.xml.parsers.DocumentBuilderFactory
* javax.xml.transform.TransformerFactory
* javax.xml.xpath.XPathFactory

## எடுத்துக்காட்டுகள்

* [javax.xml.parsers.DocumentBuilderFactory](https://docs.oracle.com/en/java/javase/17/docs/api/java.xml/javax/xml/parsers/DocumentBuilderFactory.html)
* [javax.xml.validation.SchemaFactory](https://docs.oracle.com/en/java/javase/17/docs/api/java.xml/javax/xml/validation/SchemaFactory.html)
* [javax.xml.transform.TransformerFactory](https://docs.oracle.com/en/java/javase/17/docs/api/java.xml/javax/xml/transform/TransformerFactory.html)
* [javax.xml.xpath.XPathFactory](https://docs.oracle.com/en/java/javase/17/docs/api/java.xml/javax/xml/xpath/XPathFactory.html)

## தொடர்புடைய முறைகள்

* [Factory Method](https://java-design-patterns.com/patterns/factory-method/)
* [Factory Kit](https://java-design-patterns.com/patterns/factory-kit/)

## மேலும் தகவல்

* [Design Patterns: Elements of Reusable Object-Oriented Software](https://www.amazon.com/gp/product/0201633612/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=0201633612&linkCode=as2&tag=javadesignpat-20&linkId=675d49790ce11db99d90bde47f1aeb59)
* [Head First Design Patterns: A Brain-Friendly Guide](https://www.amazon.com/gp/product/0596007124/ref=as_li_tl?ie=UTF8&camp=1789&creative=9325&creativeASIN=0596007124&linkCode=as2&tag=javadesignpat-20&linkId=6b8b6eea86021af6c8e3cd3fc382cb5b)