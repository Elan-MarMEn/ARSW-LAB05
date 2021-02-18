/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import com.google.gson.Gson;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.services.BlueprintsServicesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    @Autowired
    @Qualifier("blueprintsServices")
    BlueprintsServicesInterface blueprintsServices;

    /*GET METHODS*/

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprints() {
        try {
            Set<Blueprint> blueprintSet = blueprintsServices.multiFilter(blueprintsServices.getAllBlueprints());
            String gsonString = this.makeStringForGson(blueprintSet);
            return new ResponseEntity<>(new Gson().toJson(gsonString), HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Recurso no encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{author}", method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable String author) {
        try {
            Set<Blueprint> blueprintSet = blueprintsServices.multiFilter(blueprintsServices.getBlueprintsByAuthor(author));
            String gsonString = this.makeStringForGson(blueprintSet);
            return new ResponseEntity<>(new Gson().toJson(gsonString), HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Recurso no encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{author}/{bpname}", method = RequestMethod.GET)
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable String author, @PathVariable String bpname) {
        try {
            Blueprint blueprint = blueprintsServices.filterBlueprint(blueprintsServices.getBlueprint(author, bpname));
            Set<Blueprint> blueprintSet = new HashSet<Blueprint>();
            blueprintSet.add(blueprint);
            String gsonString = this.makeStringForGson(blueprintSet);
            return new ResponseEntity<>(new Gson().toJson(gsonString), HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Recurso no encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    /*Extra methods*/
    private String makeStringForGson(Set<Blueprint> blueprints) {
        List<Blueprint> blueprintList = new ArrayList<>(blueprints);
        String blueprintStrings = "{\"blueprints\" : ";
        for (Blueprint blueprint : blueprintList) {
            String autor = blueprint.getAuthor();
            String nombre = blueprint.getName();
            String puntos = blueprint.getPointsString();
            blueprintStrings += "{\"Autor\": \"" + autor + "\", \"Nombre\": \"" + nombre + "\", \"Puntos\": \"" + puntos + "\"}";
        }
        blueprintStrings += "}";
        return blueprintStrings;
    }

}