package com.company;

import java.util.ArrayList;

public class DClass extends DGeneralClass {
   
   // Properties
   private DConstructor cons;
   
   // Constructors
   public DClass( String name) {
      super( name);
      cons = new DConstructor((DClass) this);
   } 
   
   // Methods
   @Override
   public void extract() {
      
   }

}