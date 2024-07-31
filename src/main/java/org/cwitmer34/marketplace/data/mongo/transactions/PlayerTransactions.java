package org.cwitmer34.marketplace.data.mongo.transactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PlayerTransactions {
	private String uuid;
	private List<String> transactions;

	public final Document toBson() {
		return new Document("uuid", uuid)
						.append("transactions", transactions);
	}
}
