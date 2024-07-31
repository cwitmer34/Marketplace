package org.cwitmer34.marketplace.data.mongo.collect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PlayerCollect {
	private String playerUuid;
	private String collectUuid;
	private List<String> serializedItems;

	public final Document toBson() {
		return new Document("playerUuid", playerUuid)
						.append("collectUuid", collectUuid)
						.append("items", serializedItems);
	}
}
