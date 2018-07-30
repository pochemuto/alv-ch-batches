package ch.alv.batches.legacy.to.master;

/**
 * ISO639-1 codes ordered according to language codes, @see <a href="https://github.com/alv-ch/jobroom-api/blob/master/src/docs/asciidoc/index.adoc#language-codes>JobRoom API JobRoom API, 13. Language codes </a>
 */
enum Iso6391LanguageCode {
	UNKNOWN,
	de,
	fr,
	it,
	rm,
	en,
	es,
	pt,
	tk,
	el,
	hu,
	pl,
	cs,
	sh,
	nl,
	be, //flamand does not have iso 639-1 code, used be
	ar,
	he,
	ru,
	sv,
	ja,
	zh,
	sl,
	hr,
	da,
	ta,
	sq,
	ku,
	ch, //swiss german does not have iso 639-1 code, used ch
	sr,
	mk,
	bs,
	bg,
	no,
	sk,
	lt,
	th,
	fi,
	km,
	vi,
	ro;

	/**
	 * Returns a language code based on ordinal of given ISO639-1 code.
	 * @param code ISO639-1 code
	 * @return a language code.
	 */
	static Integer toLanguageCode(String code) {
		try {
			return valueOf(code).ordinal();
		} catch (IllegalArgumentException | NullPointerException e) {
			return null;
		}
	}
}
