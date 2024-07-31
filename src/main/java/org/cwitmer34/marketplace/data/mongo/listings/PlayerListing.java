package org.cwitmer34.marketplace.data.mongo.listings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

@Getter
@Setter
@AllArgsConstructor
public class PlayerListing {
	private String playerUuid;
	private String playerName;
	private String itemUuid;
	private String serializedItem;
	private String duration;
	private int price;

	public final Document toBson() {
		return new Document("playerUuid", playerUuid)
						.append("playerName", playerName)
						.append("itemUuid", itemUuid)
						.append("serializedItem", serializedItem)
						.append("price", price)
						.append("duration", duration);
	}

}
