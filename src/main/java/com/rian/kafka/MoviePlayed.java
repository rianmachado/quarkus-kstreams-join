package com.rian.kafka;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class MoviePlayed {

	public int id;
	public long duration;

	public MoviePlayed(int id, long duration) {
		this.id = id;
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "MoviePlayed [id=" + id + ", duration=" + duration + "]";
	}

}