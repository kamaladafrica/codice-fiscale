package it.kamaladafrica.codicefiscale;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class City {
	String name;
	String prov;

	@NonNull
	String belfiore;

}
