package de.be.aff.controller;

import de.be.aff.model.Profile;
import de.be.aff.service.ProfileService;
import de.be.aff.util.RangeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor //lombok library creates constructor for fields marked as final
//this way, we're practically having constructor injection
/* Other lombok annotations are
@Getter and @Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor
@Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog
@Data
@Builder
@Singular
@Delegate
@Value
@Accessors
@Wither
@SneakyThrows
...
 */
public class ProfileController {

     private final ProfileService profileService;

    //get request example http://localhost:8080/profiles/1
    @RequestMapping(value = "/profiles/{id}")
    public Profile getProfileUsingPathVariable(@PathVariable Integer id) {

        return new Profile("Jane", "Smith");
    }

    //get request example http://localhost:8080/profiles?id=1
    @RequestMapping(value = "/profiles/parametrized")
    public Profile getProfileUsingRequestParameter(@RequestParam Integer id) {

        return new Profile("John", "Doe");
    }


    //get request example http://localhost:8080/profiles?ageRange=19-23
    // we need to write custom converter to convert 19-23 into Range object
    @GetMapping("/profiles/ranged")
    public Profile getProfileUsingRange(@RequestParam(required = false) Range<Integer> ageRange) {

        return new Profile("John", "Doe", ageRange.getLowerBound());
    }

    //get request example http://localhost:8080/profiles
    //how to call other (non-existing) rest endpoint - reason why this method will return exception, if called
    // how to read http headers
    //this method has unit test, with ProfileService mocked
    @RequestMapping("/profiles")
    public ResponseEntity<Object> getProfiles(@RequestHeader(value = "Authorization") String token) {

        Profile[] objects;
        try {
            ResponseEntity<Profile[]> responseEntity = profileService.getProfiles(token);

            if (responseEntity != null)
                objects = responseEntity.getBody();
            else
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpStatusCodeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(e.getStatusCode().value()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (objects != null && objects.length > 0)
            return new ResponseEntity<>(Arrays.asList(objects), HttpStatus.OK);
        else
            return new ResponseEntity<>(new ArrayList<Profile>(), HttpStatus.OK);
    }

    // @InitBinder is used to customize request parameter data binding
    // It has to be defined within controller, where it's used
    // The @InitBinder annotated methods will get called on each HTTP request,
    // if we don't specify the 'value' element of this annotation (we have set value element
    // so custom handler will be set up only for controller methods which can have ageRange and blabla
    // parameters - this parameters doesn't have to be used)
    @InitBinder(value = {"ageRange","blabla"})
    private void initBinder(final WebDataBinder webdataBinder) {
        //WebDataBinder is binding parameters from web request to JavaBean objects
        //we're registering custom binder for Range class
        //when WebDataBinder tries to bind web request parameter to Range class, it will use our custom RangeConverter
        webdataBinder.registerCustomEditor(Range.class, new RangeConverter());

        //except registering custom binders, we could also do the following:
        //webDataBinder.addCustomFormatter(..);
        //webDataBinder.addValidators(..);
    }
    
}
