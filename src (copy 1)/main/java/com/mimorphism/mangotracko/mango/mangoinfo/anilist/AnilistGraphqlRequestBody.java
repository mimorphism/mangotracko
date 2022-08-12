
package com.mimorphism.mangotracko.mango.mangoinfo.anilist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnilistGraphqlRequestBody {

  private String query;
  private Object variables;
}