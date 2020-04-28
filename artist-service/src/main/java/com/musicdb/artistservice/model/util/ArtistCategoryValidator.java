package com.musicdb.artistservice.model.util;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.musicdb.artistservice.model.ArtistCategory;

public class ArtistCategoryValidator implements ConstraintValidator<ArtistCategoryValidation, ArtistCategory> {

	private ArtistCategory[] artistCategory;

	@Override
	public void initialize(ArtistCategoryValidation constraint) {
		this.artistCategory = constraint.anyOf();
	}

	@Override
	public boolean isValid(ArtistCategory value, ConstraintValidatorContext context) {
		return value == null || Arrays.asList(artistCategory).contains(value);
	}

}
