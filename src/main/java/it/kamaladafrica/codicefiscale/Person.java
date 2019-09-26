package it.kamaladafrica.codicefiscale;

import java.time.LocalDate;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Person {

	@NonNull
	String firstname;

	@NonNull
	String lastname;

	@NonNull
	LocalDate birthDate;

	@NonNull
	City city;

	boolean isFemale;

}
