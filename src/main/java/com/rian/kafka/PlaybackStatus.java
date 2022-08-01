package com.rian.kafka;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PlaybackStatus {


	public String name;
	public String director;
	public String reproducao;
	
	public PlaybackStatus(String name, String director, long reproducao) {
		this.name = name;
		this.director = director;
		this.reproducao = reproducao + " Minutos" ;
	}


}