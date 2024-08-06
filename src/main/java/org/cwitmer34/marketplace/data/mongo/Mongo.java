package org.cwitmer34.marketplace.data.mongo;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.cwitmer34.marketplace.MarketplaceMain;
import org.cwitmer34.marketplace.util.ConsoleUtil;

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

		final String uri = MarketplaceMain.getPlugin().getConfig().getString("mongo-uri");


		final MongoClientSettings settings = MongoClientSettings.builder()
						.applyConnectionString(new ConnectionString(uri))
						.serverApi(serverApi).retryReads(false).retryWrites(false)
						.uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
						.build();
		try {
			this.client = MongoClients.create(settings);
			this.database = client.getDatabase("Marketplace");
			this.initCollections();
		} catch (Exception e) {
			ConsoleUtil.severe(e.getMessage());
			ConsoleUtil.severe("Failed to connect to MongoDB. Please ensure your MongoDB URI is valid.");
			ConsoleUtil.severe("Disabling plugin...");
			MarketplaceMain.getPlugin().getServer().getPluginManager().disablePlugin(MarketplaceMain.getPlugin());
		}
	}

	private void initCollections() {
		this.listings = database.getCollection("Listings");
		this.collect = database.getCollection("Collections");
		this.transactions = database.getCollection("Transactions");
	}
}
