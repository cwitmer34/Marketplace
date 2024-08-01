package org.cwitmer34.marketplace.discord;

import javax.net.ssl.HttpsURLConnection;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.cwitmer34.marketplace.TrialMarketplace;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Credits to https://gist.github.com/k3kdude/fba6f6b37594eae3d6f9475330733bdb
 */
@Getter
@Setter
public class DiscordWebhook {
	private final String url = TrialMarketplace.getPlugin().getConfig().getString("discord-webhook");
	private String content;
	private String username;
	private String avatarUrl;
	private boolean tts;
	private List<EmbedObject> embeds = new ArrayList<>();

	public void addEmbed(final EmbedObject embed) {
		this.embeds.add(embed);
	}

	@SneakyThrows
	public void execute() {
		if (this.content == null && this.embeds.isEmpty()) {
			throw new IllegalArgumentException("Set content or add at least one EmbedObject");
		}

		final JSONObject json = new JSONObject()
						.put("content", this.content)
						.put("username", this.username)
						.put("avatar_url", this.avatarUrl)
						.put("tts", this.tts);

		if (!this.embeds.isEmpty()) {
			final JSONArray embedArray = new JSONArray();
			for (final EmbedObject embed : this.embeds) {
				embedArray.put(embed.toJSON());
			}
			json.put("embeds", embedArray);
		}

		sendPostRequest(json);
	}

	private void sendPostRequest(final JSONObject json) throws IOException {
		final URL url = new URL(this.url);
		final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

		connection.addRequestProperty("Content-Type", "application/json");
		connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");

		try (final OutputStream stream = connection.getOutputStream()) {
			stream.write(json.toString().getBytes());
			stream.flush();
		}

		connection.getInputStream().close(); // Necessary to complete the request
		connection.disconnect();
	}

	public static class EmbedObject {
		private String title;
		private String description;
		private String url;
		private Color color;
		private Footer footer;
		private Thumbnail thumbnail;
		private Image image;
		private Author author;
		private final List<Field> fields = new ArrayList<>();

		public JSONObject toJSON() {
			JSONObject jsonEmbed = new JSONObject()
							.put("title", title)
							.put("description", description)
							.put("url", url);

			if (color != null) {
				jsonEmbed.put("color", color.getRGB() & 0xFFFFFF); // Convert Color to RGB
			}

			if (footer != null) jsonEmbed.put("footer", footer.toJSON());
			if (image != null) jsonEmbed.put("image", image.toJSON());
			if (thumbnail != null) jsonEmbed.put("thumbnail", thumbnail.toJSON());
			if (author != null) jsonEmbed.put("author", author.toJSON());

			final JSONArray jsonFields = new JSONArray();

			for (final Field field : fields) {
				jsonFields.put(field.toJSON());
			}
			jsonEmbed.put("fields", jsonFields);

			return jsonEmbed;
		}

		public static class Footer {
			private final String text;
			private final String iconUrl;

			public Footer(final String text, final String iconUrl) {
				this.text = text;
				this.iconUrl = iconUrl;
			}

			public JSONObject toJSON() {
				return new JSONObject()
								.put("text", text)
								.put("icon_url", iconUrl);
			}
		}

		public static class Thumbnail {
			private String url;

			public Thumbnail(String url) {
				this.url = url;
			}

			public JSONObject toJSON() {
				return new JSONObject()
								.put("url", url);
			}
		}

		public static class Image {
			private String url;

			public Image(String url) {
				this.url = url;
			}

			public JSONObject toJSON() {
				return new JSONObject()
								.put("url", url);
			}
		}

		public static class Author {
			private final String name;
			private final String url;
			private final String iconUrl;

			public Author(final String name, final String url, final String iconUrl) {
				this.name = name;
				this.url = url;
				this.iconUrl = iconUrl;
			}

			public JSONObject toJSON() {
				return new JSONObject()
								.put("name", name)
								.put("url", url)
								.put("icon_url", iconUrl);
			}
		}

		public static class Field {
			private final String name;
			private final String value;
			private final boolean inline;

			public Field(final String name, final String value, final boolean inline) {
				this.name = name;
				this.value = value;
				this.inline = inline;
			}

			public JSONObject toJSON() {
				return new JSONObject()
								.put("name", name)
								.put("value", value)
								.put("inline", inline);
			}
		}
	}
}
