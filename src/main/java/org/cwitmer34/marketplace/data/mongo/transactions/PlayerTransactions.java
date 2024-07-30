package org.cwitmer34.marketplace.data.mongo.transactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class PlayerTransactions {
	private String uuid;
	private String duration;
	private List<String> transactions;

	public final Document toBson() {
		return new Document("uuid", uuid)
						.append("transactions", transactions);
	}
}
