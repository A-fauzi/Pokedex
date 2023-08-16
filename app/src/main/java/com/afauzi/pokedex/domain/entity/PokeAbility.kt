package com.afauzi.pokedex.domain.entity

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PokeAbility(

	@field:SerializedName("effect_entries")
	val effectEntries: List<EffectEntriesItem?>? = null,

	@field:SerializedName("generation")
	val generation: Generation? = null,

	@field:SerializedName("is_main_series")
	val isMainSeries: Boolean? = null,

	@field:SerializedName("names")
	val names: List<NamesItem?>? = null,

	@field:SerializedName("pokemon")
	val pokemon: List<PokemonItem?>? = null,

	@field:SerializedName("flavor_text_entries")
	val flavorTextEntries: List<FlavorTextEntriesItem?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

) : Parcelable

@Parcelize
data class EffectEntriesItem(

	@field:SerializedName("short_effect")
	val shortEffect: String? = null,

	@field:SerializedName("effect")
	val effect: String? = null,

	@field:SerializedName("language")
	val language: Language? = null
) : Parcelable

@Parcelize
data class Generation(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class NamesItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("language")
	val language: Language? = null
) : Parcelable

@Parcelize
data class Language(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable

@Parcelize
data class PokemonItem(

	@field:SerializedName("pokemon")
	val pokemon: Pokemon? = null,

	@field:SerializedName("is_hidden")
	val isHidden: Boolean? = null,

	@field:SerializedName("slot")
	val slot: Int? = null
) : Parcelable

@Parcelize
data class FlavorTextEntriesItem(

	@field:SerializedName("version_group")
	val versionGroup: VersionGroup? = null,

	@field:SerializedName("language")
	val language: Language? = null,

	@field:SerializedName("flavor_text")
	val flavorText: String? = null
) : Parcelable