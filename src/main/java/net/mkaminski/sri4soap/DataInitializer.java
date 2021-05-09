package net.mkaminski.sri4soap;

import lombok.RequiredArgsConstructor;
import net.mkaminski.sri4soap.model.Employee;
import net.mkaminski.sri4soap.repo.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);

    private final EmployeeRepository employeeRepository;

    public void initData(){
        Employee e1 = new Employee();
        e1.setFirstName("Jan");
        e1.setLastName("Kowalski");
        e1.setJob("Clerk");
        e1.setBirthDate(LocalDate.of(1990,01,01));
        Employee e2 = new Employee();
        e2.setFirstName("Adam");
        e2.setLastName("Nowak");
        e2.setJob("Manager");
        e2.setBirthDate(LocalDate.of(1991,01,01));
        Employee e3 = new Employee();
        e3.setFirstName("Anna");
        e3.setLastName("Iksińska");
        e3.setJob("Assistant");
        e3.setBirthDate(LocalDate.of(1992,01,01));
        employeeRepository.saveAll(Arrays.asList(e1,e2,e3));
        LOG.info("Data initialized");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event){
        initData();
    }
}
