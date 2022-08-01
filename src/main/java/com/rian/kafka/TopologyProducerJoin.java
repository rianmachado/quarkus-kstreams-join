package com.rian.kafka;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.jboss.logging.Logger;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;

@ApplicationScoped
public class TopologyProducerJoin {

	@Inject
	Logger logger;
	
	private static final String MOVIES_TOPIC = "movies-v2";
	private static final String PLAY_MOVIES_TOPIC = "playtimemovies";
	private final String OUTPUT_JOIN = "output-join";

	@Produces
	public Topology getTopChartsJoin() {
		

		final StreamsBuilder builder = new StreamsBuilder();
		final ObjectMapperSerde<Movie> movieSerder = new ObjectMapperSerde<>(Movie.class);
		final ObjectMapperSerde<PlaybackStatus> playbackStatusSerder = new ObjectMapperSerde<>(PlaybackStatus.class);
		final ObjectMapperSerde<MoviePlayed> moviePlayedSerder = new ObjectMapperSerde<>(MoviePlayed.class);

		final GlobalKTable<Integer, Movie> moviesTable = builder.globalTable(MOVIES_TOPIC,
				Consumed.with(Serdes.Integer(), movieSerder));
		
		final KStream<String, MoviePlayed> playEvents = builder.stream(PLAY_MOVIES_TOPIC,
				Consumed.with(Serdes.String(), moviePlayedSerder));
		
		playEvents.map((key, value) -> KeyValue.pair(value.id, value))
		.join(moviesTable, (movieId, moviePlayedId) -> movieId, (moviePlayed, movie) -> new PlaybackStatus(movie.name,movie.director,moviePlayed.duration))
		.to(OUTPUT_JOIN, Produced.with(Serdes.Integer(), playbackStatusSerder));
		
		logger.info(" Output Join SUCCESSFUL.... ");
		
		return builder.build();

	}
}