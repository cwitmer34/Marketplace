package org.cwitmer34.marketplace.data.mongo.collect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.entity.Item;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PlayerCollect {
	private String uuid;
	private List<String> items;

	public final Document toBson() {
		return new Document("uuid", uuid)
						.append("items", items);
	}
}
