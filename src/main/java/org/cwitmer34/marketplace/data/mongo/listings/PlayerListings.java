package org.cwitmer34.marketplace.data.mongo.listings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class PlayerListings {
	private String uuid;
	private Map<String, Map<String, Double>> items;

	public final Document toBson() {
		return new Document("uuid", uuid)
						.append("items", items);
	}
}
