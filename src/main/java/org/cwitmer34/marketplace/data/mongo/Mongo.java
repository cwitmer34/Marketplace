package org.cwitmer34.marketplace.data.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.cwitmer34.marketplace.TrialMarketplace;

@Getter
public class Mongo {

	public MongoCollection<Document> listings, collect, transactions;
	private MongoDatabase database;
	private MongoClient client;

	public Mongo() {
		this.init();
	}

	public void init() {
		final ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();

		final String uri = TrialMarketplace.getPlugin().getConfig().getString("mongo-uri");

		final MongoClientSettings settings = MongoClientSettings.builder()
						.applyConnectionString(new ConnectionString(uri))
						.serverApi(serverApi)
						.uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
						.build();

		this.client = MongoClients.create(settings);
		this.database = client.getDatabase("Marketplace");

		this.initCollections();
	}

	private void initCollections() {
		this.listings = database.getCollection("Listings");
		this.collect = database.getCollection("Collections");
		this.transactions = database.getCollection("Transactions");
	}
}
