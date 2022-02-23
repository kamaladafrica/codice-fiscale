# Codice Fiscale
 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/it.kamaladafrica/codice-fiscale/badge.svg)](https://maven-badges.herokuapp.com/maven-central/it.kamaladafrica/codice-fiscale)
 
Yet another **Codice Fiscale** (italian tax code) calculator.
Only natural person tax code calculation is supported (16 chars).

*Codice fiscale* is composed by 5 parts:

* Lastname part
* Firstname part
* Birth date and sex part
* Belfiore part
* Control part

You can access every part:

```java
CodiceFiscale cf = CodiceFiscale.of("RSSMRA75C22H501I");

Part firstnamePart = cf.getFirstname();
System.out.println(firstnamePart.getValue()); // MRA

DatePart datePart = cf.getDate();
System.out.println(datePart.isFemale()); // false
```

## Features

* [Calculation](#calculation) (codice fiscale from person data)
* [Reverse calculation](#reverse-calculation) (person data from codice fiscale)
* [Validation](#validation)
* [Comparison and compatibility](#comparison-and-compatibility)
* [Support for omocode levels](#support-for-omocode-levels)


## Usage

In order to see it in action, check the [tests](https://github.com/kamaladafrica/codice-fiscale/tree/master/src/test/java/it/kamaladafrica/codicefiscale)


### Calculation

You can calulate the tax code by person data.

```java
CityByName cities = CityProvider.ofDefault();

City rome = cities.findByName("Roma");
// City rome = City.builder().name("ROMA").prov("RM").belfiore("H501").build();

Person person =	Person.builder()
	.firstname("Mario")
	.lastname("Rossi")
	.birthDate(LocalDate.of(1975, 3, 22))
	.isFemale(false)
	.city(rome)
	.build();

CodiceFiscale cf = CodiceFiscale.of(PERSON);

System.out.println(cf.getValue()); // RSSMRA75C22H501I
```


#### Cities

City objects can be built by hands:

```java
City city = City.builder().name("ROMA").prov("RM").belfiore("H501").build();
```

or, as suggested, using a `CityProvider` object.

A CityProvider implements the two interfaces `CityByName` and `CityByBelfiore`: the former is used during calculation, the latter during reverse calculation.

```java
CityByName cities = CityProvider.ofDefault();
City rome = cities.findByName('Roma');
// City rome = cities.findByBelfiore('H501');
```

A default `CityProvider` is provided but you can use your own `CityProvider` if you want.

```java
CityProvider cities = CityProvider.ofDefault();
// CityByName cities = CityProvider.ofDefault();
// CityByBelfiore cities = CityProvider.ofDefault();
```
Default `CityProvider` reads cities from [official Istat file](https://github.com/kamaladafrica/codice-fiscale/blob/master/src/main/resources/istat.csv), downloaded from https://www.istat.it/storage/codici-unita-amministrative/Elenco-codici-statistici-e-denominazioni-delle-unita-territoriali.zip

Of course, you can provide your own set of cities creating a `CityProviderImpl` instance with a `Supplier<Set<City>>` object that supplies you own `City` set.

```java
Set<City> myCities = ...;
CityProvider cities = CityProvider.of(() -> myCities);
City city = cities.findByName('Roma');
```


### Reverse calculation

You can figure out some of the person data from a *codice fiscale*.

```java
CodiceFiscale cf = CodiceFiscale.of("RSSMRA75C22H501I");

// or if you want your own implementation of CityProvider
//
// CityByBelfiore cities = new MyCityProvider();
// CodiceFiscale cf = CodiceFiscale.of("RSSMRA75C22H501I", cities);

Person person = cf.getPerson();

System.out.println(person.getCity().getName()); // ROMA
System.out.println(person.isFemale()); // false
System.out.println(person.getFirstname()); // MRA // you can't figure out real first name
```

Of course you can figure out only sex, birth date and city of birth, but no the real first name and last name.


### Validation

You can validate a *codice fiscale* by building a `CodiceFiscale` instance or using the static method `CodiceFiscale.validate(String)` or `CodiceFiscale.isFormatValid(String)`.

Creating an instance of `CodiceFiscale` using `of` performs a deep validation checking the validity of the Belfiore part.
On the other hand `validate` (that throws exception if code is invalid) and `isFormatValid` (return true if code is valid, false otherwise) only check that *codice fiscale* is "grammatically" correct and the control char matches.

```java

CodiceFiscale cf = CodiceFiscale.of("RSSMRA75C22Z999I"); // throws exception due to both bad Belfiore part and bad control char
CodiceFiscale.isFormatValid("RSSMRA75C22H501I"); // returns true
CodiceFiscale.isFormatValid("RSSMRA75C22H501X"); // returns false due to bad control char (should be I)
CodiceFiscale.validate("RSSMRA75C22H501X"); // throws exception due to bad control char
```


### Comparison and compatibility

You can compare two *codice fiscale* even thought they have different omocode levels.

```java
CodiceFiscale cf1 = CodiceFiscale.of("RSSMRA75C22H501I"); // no omocode
CodiceFiscale cf2 = CodiceFiscale.of("RSSMRA75C22H5LML"); // omocode level 2
cf1.isEqual(cf2); // returns true (ignore omocode level)
cf1.isEqual(cf2, false); // return false (compares omocode level)

cf1.isEqual("RSSMRA75C22H5LML"); // returns true
```

Also you can check if a *codice fiscale* is compatible with a person data set.

```java
Person person =	Person.builder()
	.firstname("Mario")
	.lastname("Rossi")
	.birthDate(LocalDate.of(1975, 3, 22))
	.isFemale(false)
	.city(rome)
	.build();

CodiceFiscale cf = CodiceFiscale.isCompatible("RSSMRA75C22H5LML", person); // return true
```

### Support for omocode levels

A *codice fiscale* can be omocodic if two or more people share the same codice fiscale ([learn more on Wikipedia](https://it.wikipedia.org/wiki/Omocodia)).

Level indicates how many letters are changed. A letter is changed when another person has the same *codice fiscale*, so a omocode level 2 means that at least 3 people have the same *codice fiscale*.

Level is 0 <= level <= 7.

You can change omocode level using `toOmocodeLevel`. The method `normalized` is the same as `toOmocodeLevel(0)`

```java
Person person =	Person.builder()
	.firstname("Mario")
	.lastname("Rossi")
	.birthDate(LocalDate.of(1975, 3, 22))
	.isFemale(false)
	.city(rome)
	.build();

CodiceFiscale cf0 = CodiceFiscale.of(person).toOmocodeLevel(2);
System.out.println(cf0.getValue()); // RSSMRA75C22H501I
System.out.println(cf0.getOmocodeLevel()); // 0

CodiceFiscale cf2 = CodiceFiscale.of(person).toOmocodeLevel(2);
System.out.println(cf2.getValue()); // RSSMRA75C22H5LML
System.out.println(cf2.getOmocodeLevel()); // 2

System.out.println(cf0.isEqual(cf2)); // true

CodiceFiscale cf = cf2.normalized();
System.out.println(cf.getValue()); // RSSMRA75C22H501I
System.out.println(cf.getOmocodeLevel()); // 0
```

## Getting started

All you need to do is to declare the dependency

```xml
<dependency>
  <groupId>it.kamaladafrica</groupId>
  <artifactId>codice-fiscale</artifactId>
  <version>...</version>
</dependency>
```

## Contributing

Here are some ways for you to contribute:

* Create [GitHub tickets](https://github.com/kamaladafrica/codice-fiscale/issues) for bugs or new features and comment on the ones that you are interested in.
* GitHub is for social coding: if you want to write code, we encourage contributions [through pull requests](https://help.github.com/articles/creating-a-pull-request)
  from [forks of this repository](https://help.github.com/articles/fork-a-repo).
  If you want to contribute code this way, please reference a GitHub ticket as well covering the specific issue you are addressing.

### Update italian cities names, provinces and codes

Cities names, provinces and codes are stored in a csv file located in `src/main/resources/italia.csv`.

In order to update csv with latest changes from [Anagrafe Nazione della Popolazione Residente](https://www.anpr.interno.it) easily, you can take advantage of the script `scripts/get_belfiore_anpr.sh`.
It prints ready to use csv (filtered and formatted) to the standard output so you can redirect it to a file, for example.

Usage:
```sh
$ scripts/get_belfiore_anpr.sh > src/main/resources/italia.csv
```
