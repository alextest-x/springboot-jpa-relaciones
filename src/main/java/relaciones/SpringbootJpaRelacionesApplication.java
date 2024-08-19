package relaciones;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import relaciones.entities.*;
import relaciones.repositories.*;

import java.util.*;

@SpringBootApplication
public class SpringbootJpaRelacionesApplication implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ClientDetailsRepository clientDetailsRepository;

    @Autowired
    private StudentRepository studentRepository;

   @Autowired
    private CourseRepository courseRepository;


    public static void main(String[] args) {
        SpringApplication.run(SpringbootJpaRelacionesApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
      //  manyToOne();         //Invoice
      //  manyToOneFindById(); //Invoice
      //  oneToMany();        //address
      //  oneToManyFindById();  //busca el cliente en Bd y actualiza las direcciones
      //  removeAddress();
      //  removeAddressFindById();
      //  oneToManyInvoiceBidireccional();
        //   oneToManyInvoiceBidireccionalFindById(); //cliente existente en la BD

        //removeInvoiceBidireccionalFindById();  //eliminar

        //removeInvoiceBidireccionalFindById2();  //eliminar

        //removeInvoiceBidireccional();

        //removeInvoiceBidireccional2();

        //ejemplo @OneToOne

        //oneToOne();     //la llave foranea esta en la tabla ClientDetails
        //oneToOne2();    //la llave foranea esta en la tabla client

        //obtiene un cliente por id de la bd @OneToOne
        //oneToOneFindById();
        //oneToOneBidirecional();
        //oneToOneBidirecionalFindById(); //buscamos por id


        //ejemplo de @ManyToMany
        //manyToMany(); //asignamos cursos a estudiantes
        // manyToManyFind(); //buscamos los alumnos en bd
        //manyToManyRemoveFind(); //eliminamos un curso
        //manyToManyRemove();
        //manyToManyBidirecionalRemove();   //eliminamos en ambos sentidos
        //manyToManyBidereccionalFind();     //buscando en forma bidireccional
        manyToManyRemoveBidereccionalFind();  //elimina en forma bidireccional

    }


    @Transactional
    public void manyToManyRemoveBidereccionalFind(){

        Optional<Student> studentOptional1 = studentRepository.findOneWithCourses(1L);
        Optional<Student> studentOptional2 = studentRepository.findOneWithCourses(2L);

        //obtenemos a los alumnos de la bd
        Student student1 = studentOptional1.get();
        Student student2 = studentOptional2.get();

        /** los buscamos los cursos en la base de datos
         //Course course1 = courseRepository.findById(1L).get();
         //Course course2 = courseRepository.findById(2L).get();

         //hay que cambiar el findById(); para que no de error por carga lazily por la relacion inversa
         //hay que llamar la metodo findOneWithStudents de CourseRepository.java
         **/

        Course course1 = courseRepository.findOneWithStudents(1L).get();
        Course course2 = courseRepository.findOneWithStudents(2L).get();

        student1.addCourse(course1);
        student1.addCourse(course2);
        student2.addCourse(course2);

        //guardamos los registros
        studentRepository.saveAll(List.of(student1, student2));
        System.out.println("============ estudiantes ============");
        System.out.println("student 1 = " + student1);
        System.out.println("student 2 = " + student2);
        System.out.println("======================================");

        //buscamos el estudiante con id = 3
        Optional<Student> studentOptionalDb = studentRepository.findOneWithCourses(1L);
        if (studentOptionalDb.isPresent()){

            Student studentDb = studentOptionalDb.get();
            //solo obtiene el curso y lo elimina el curso del estudiante
            //para consulta bidirecional da error por el Lazily en curso solo pasa en consola en cweb service no ocurre
            //solucion hacer @query el join en repositoryCurso para atraer todos los datos
            //Optional<Course> courseOptionalDb = courseRepository.findById(3L);
            Optional<Course> courseOptionalDb = courseRepository.findOneWithStudents(1L);

            //si esta alumno
            if(courseOptionalDb.isPresent()){
                Course courseDb = courseOptionalDb.get();  //obtiene el curso
                studentDb.removeCourse(courseDb);         //eliminamos el curso

                studentRepository.save(studentDb);
                System.out.println("Curso Eliminando: " +courseDb);
                System.out.println("Curso Actualizado: " +studentDb);
            }
        }

    }


    @Transactional
    public void manyToManyBidereccionalFind(){
        //buscamos en los alumnos en la bd
        //hay que cambiar el findById(); para que no de error por carga lazily
        //hay que llamar la metodo findOneWithCourses de StudentRepository.java por la relacion inversa
        //Optional<Student> studentOptional1 = studentRepository.findById(1L);
        //Optional<Student> studentOptional2 = studentRepository.findById(2L);

        Optional<Student> studentOptional1 = studentRepository.findOneWithCourses(1L);
        Optional<Student> studentOptional2 = studentRepository.findOneWithCourses(2L);

        //obtenemos a los alumnos de la bd
        Student student1 = studentOptional1.get();
        Student student2 = studentOptional2.get();

        /** los buscamos los cursos en la base de datos
        //Course course1 = courseRepository.findById(1L).get();
        //Course course2 = courseRepository.findById(2L).get();

        //hay que cambiar el findById(); para que no de error por carga lazily por la relacion inversa
        //hay que llamar la metodo findOneWithStudents de CourseRepository.java
        **/

        Course course1 = courseRepository.findOneWithStudents(1L).get();
        Course course2 = courseRepository.findOneWithStudents(2L).get();

        student1.addCourse(course1);
        student1.addCourse(course2);
        student2.addCourse(course2);

        //guardamos los registros
        studentRepository.saveAll(List.of(student1, student2));
        System.out.println("============ estudiantes ============");
        System.out.println("student 1 = " + student1);
        System.out.println("student 2 = " + student2);
        System.out.println("======================================");
    }



    //remove bidirecional en ambos sentidos
    @Transactional
    public void manyToManyBidirecionalRemove(){

        Student student1 = new Student("Jano", "Pura");
        Student student2 = new Student("Erba", "Doe");

        Course course1 = new Course("Curso Java Master", "Andres");
        Course course2 = new Course("Curso Spring Boot", "Andres");

        student1.addCourse(course1);
        student1.addCourse(course2);
        student2.addCourse(course2);

        studentRepository.saveAll(List.of(student1, student2));

        System.out.println("============ estudiantes ============");
        System.out.println("student 1 = " + student1);
        System.out.println("student 2 = " + student2);
        System.out.println("======================================");

        //buscamos el estudiante con id = 3
        Optional<Student> studentOptionalDb = studentRepository.findOneWithCourses(3L);
        if (studentOptionalDb.isPresent()){

            Student studentDb = studentOptionalDb.get();
            //solo obtiene el curso y lo elimina el curso del estudiante
            //para consulta bidirecional da error por el Lazily en curso solo pasa en consola en cweb service no ocurre
            //solucion hacer @query el join en repositoryCurso para atraer todos los datos
            //Optional<Course> courseOptionalDb = courseRepository.findById(3L);
            Optional<Course> courseOptionalDb = courseRepository.findOneWithStudents(3L);

            //si esta alumno
            if(courseOptionalDb.isPresent()){
                Course courseDb = courseOptionalDb.get();  //obtiene el curso
                studentDb.removeCourse(courseDb);         //eliminamos el curso

                studentRepository.save(studentDb);
                System.out.println("Curso Eliminando: " +courseDb);
                System.out.println("Curso Actualizado: " +studentDb);
            }
        }
    }


    // Eliminamos en ambos sentidos
    @Transactional
    public void manyToManyBidireccional(){

        //creamos los estudiantes
        Student student1 = new Student("Jano", "Pura");
        Student student2 = new Student("Erba", "Doe");

        //creamos los cursos
        Course course1 = new Course("Java Master", "Andres");
        Course course2 = new Course("Spring Boot", "Andres");

        //creamos la relacion inversa con el add
        student1.addCourse(course1);
        student1.addCourse(course2);
        student2.addCourse(course2);

        //persistir el estudiante y en cascada crea el studiante1 y le pone los 2 cursos
        // y al studiante2 le pone un solo curso
        // y spring crea una tabla intermedia que contenga la llave foranea studiante y la fK de curso
        //saveAll() guarda varios studiantes es un iterable puede poner set o list
        //studentRepository.saveAll(Set.of(student1, student2));
        studentRepository.saveAll(List.of(student1, student2));

        //inserta los estudiantes solo se hace el save  de los studiantes no de los cursos
        //los cursos se insertan en forma automatica con el cascade

        System.out.println("============ estudiantes ============");
        System.out.println("student 1 = " + student1);
        System.out.println("student 2 = " + student2);
        System.out.println("======================================");

    }


    @Transactional
    public void manyToManyRemove(){

        Student student1 = new Student("Jano", "Pura");
        Student student2 = new Student("Erba", "Doe");

        Course course1 = new Course("Java Master", "Andres");
        Course course2 = new Course("Spring Boot", "Andres");

        student1.setCourses(Set.of(course1, course2));
        student2.setCourses(Set.of(course2));

        studentRepository.saveAll(List.of(student1, student2));

        System.out.println("============ estudiantes ============");
        System.out.println("student 1 = " + student1);
        System.out.println("student 2 = " + student2);
        System.out.println("======================================");

        //buscamos el estudiante con id = 3
        Optional<Student> studentOptionalDb = studentRepository.findOneWithCourses(3L);
        if (studentOptionalDb.isPresent()){

            Student studentDb = studentOptionalDb.get();
            //solo obtiene el curso y lo elimina
            Optional<Course> courseOptionalDb = courseRepository.findById(3L);

            //si esta alumno
            if(courseOptionalDb.isPresent()){
                Course courseDb = courseOptionalDb.get();  //obtiene el curso
                studentDb.getCourses().remove(courseDb);  //eliminamos el curso

                studentRepository.save(studentDb);
                System.out.println("Curso Eliminando: " +courseDb);
                System.out.println("Curso Actualizado: " +studentDb);
            }
        }
    }





    // eliminar el curso2 del studiante 1
    @Transactional
    public void manyToManyRemoveFind(){

        //buscamos en los alumnos en la bd
        Optional<Student> studentOptional1 = studentRepository.findById(1L);
        Optional<Student> studentOptional2 = studentRepository.findById(2L);

        //obtenemos a los alumnos de la bd
        Student student1 = studentOptional1.get();
        Student student2 = studentOptional2.get();

        //los buscamos los cursos en la base de datos
        Course course1 = courseRepository.findById(1L).get();
        Course course2 = courseRepository.findById(2L).get();

        //asignamos los cursos a los alumnos
        student1.setCourses(Set.of(course1, course2));
        student2.setCourses(Set.of(course2));

        //guardamos los cursos
        studentRepository.saveAll(List.of(student1, student2));

        //guardamos los registros
        studentRepository.saveAll(List.of(student1, student2));
        System.out.println("============ estudiantes ============");
        System.out.println("student 1 = " + student1);
        System.out.println("student 2 = " + student2);
        System.out.println("======================================");

        //buscamos el estudiante con id = 1
        //Optional<Student> studentOptionalDb = studentRepository.findById(1L);
        Optional<Student> studentOptionalDb = studentRepository.findOneWithCourses(1L);
        if (studentOptionalDb.isPresent()){              //o podemos poner
           Student studentDb = studentOptionalDb.get(); //Student studentDb = studentOptionalDb.orElseThrow();

            //solo obtiene el curso y lo elimina
            Optional<Course> courseOptionalDb = courseRepository.findById(2L);
            //si esta alumno
            if(courseOptionalDb.isPresent()){
               Course courseDb = courseOptionalDb.get(); //eliminamos el curso
                studentDb.getCourses().remove(courseDb);

                studentRepository.save(studentDb);
                System.out.println("Curso Eliminando: " +courseDb);
                System.out.println("Curso Actualizado: " +studentDb);

                /** sale error: failed to lazily initialize a collection of role: relaciones.entities.Student.courses: could not initialize proxy - no Session
                hay que poner el hascode en la clase entity  de Course.java
                para que pueda comparar el id, nombre e instructor
                 error al obtener los cursos por carga perezosa es donde hace la conexion y la cierra antes de hacer un query

                 solucion:
                 1. poner una configuracion en el properties

                 2. agregar el Eager en el Fetch de la clase Studient.java (clase entity)
                    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE Eager} )
                    fetch = FetchType.LAZY  en el metodo  private Set<Course> courses;
                    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE,})

                    3. que traiga los cursos en una sola consulta
                 **/
            }

        }

    }




    @Transactional
    public void manyToManyFind(){
        //buscamos en los alumnos en la bd
         Optional<Student> studentOptional1 = studentRepository.findById(1L);
         Optional<Student> studentOptional2 = studentRepository.findById(2L);

         //obtenemos a los alumnos de la bd
         Student student1 = studentOptional1.get();
         Student student2 = studentOptional2.get();

          //los buscamos los cursos en la base de datos
         Course course1 = courseRepository.findById(1L).get();
         Course course2 = courseRepository.findById(2L).get();

         //asignamos los cursos a los alumnos
         student1.setCourses(Set.of(course1, course2));
         student2.setCourses(Set.of(course2));

         //guardamos los cursos
          studentRepository.saveAll(List.of(student1, student2));

         //guardamos los registros
        studentRepository.saveAll(List.of(student1, student2));
        System.out.println("============ estudiantes ============");
        System.out.println("student 1 = " + student1);
        System.out.println("student 2 = " + student2);
        System.out.println("======================================");
    }




    /**
     *  A un estudiante e asisgnamos unos cursos y a otro estudiante le asignamos un curso
     *
     */
    @Transactional
    public void manyToMany(){

        //creamos losstudiantes y los cursos
        Student student1 = new Student("Jano", "Pura");
        Student student2 = new Student("Erba", "Doe");
        Course course1 = new Course("Java Master", "Andres");
        Course course2 = new Course("Spring Boot", "Andres");

        //creamos la relacion
        student1.setCourses(Set.of(course1, course2));
        student2.setCourses(Set.of(course2));

        //persistir el estudiante y en cascada crea el studiante1 y le pone los 2 cursos
        // y al studiante2 le pone un solo curso
        // y spring crea una tabla intermedia que contenga la llave foranea studiante y la fK de curso
        //saveAll() guarda varios studiantes es un iterable puede poner set o list
        //studentRepository.saveAll(Set.of(student1, student2));
        studentRepository.saveAll(List.of(student1, student2));

        //inserta los estudiantes solo se hace el save  de los studiantes no de los cursos
        //los cursos se insertan en forma automatica con el cascade

        System.out.println("============ estudiantes ============");
        System.out.println("student 1 = " + student1);
        System.out.println("student 2 = " + student2);
        System.out.println("======================================");

    }



    @Transactional
    public void oneToOneBidirecionalFindById(){

        //lo pone en automatico el optional el IDE
        //Client client = clienteRepository.findById(2L);

        // Optional<Client> clientOptional = clienteRepository.findById(2L);
        // hay que poner el findOne para que traiga toda las relaciones  y no sea carga perezosa Lazzy
        // busca el detalle y lo guarda
        Optional<Client> clientOptional = clienteRepository.findOne(1L);
        clientOptional.ifPresent(client -> {
            ClientDetails clientDetails = new ClientDetails(true, 5000);
            client.setClientDetails(clientDetails);
            clienteRepository.save(client);
            System.out.println("cliente guadar: " + client);

        });

    }




    @Transactional
    public void oneToOneBidirecional(){


        Client client = new Client("Svetlana", "Svet");

        ClientDetails clientDetails = new ClientDetails(true, 5000);


        //en ambos lados guarda
        //cliente guarda al detalle
        client.setClientDetails(clientDetails);

        //el detalle clientDetails guarda al cliente donde cliente es el padre no el dueño de la relacion
        //clientDetails.setClient(client); //lo movemos a la clase entity en el metodo set setClientDetails de la clase Client


        clienteRepository.save(client);

        System.out.println("cliente guadar: " + client);


    }




    //la llave foranea esta en la tabla client
    @Transactional
    public void oneToOneFindById(){

        //creamos el cliente detalle
        ClientDetails clientDetails = new ClientDetails(true, 5000);
        clientDetailsRepository.save(clientDetails);


        //creamos el cliente y le pasamos el detalle al cliente
        //Client client = clienteRepository.findById(2L);
        //Optional<Client> clientOptional = clienteRepository.findById(2L); se comenta para que no error
        Optional<Client> clientOptional = clienteRepository.findOne(2L); //trae todo con el join

        clientOptional.ifPresent(client -> {
            client.setClientDetails(clientDetails);
            clienteRepository.save(client);


            //aqui da error al imprimir en el toString de la clase Client por carga perezosa failed to lazily initialize a collection of role:
            //entonces no tiene la sesion abierta para hacer la consulta en la Bd
            //soluciones
            //1) poner en el properties la configuracion
            //2) quitar del to string invoices y addresses de la clase Client 
            //3) consulta personalizada findOne() trae todos los campos
            System.out.println("cliente guadar: " + client);

        });


    }




    //la llave foranea esta en la tabla client
    @Transactional
    public void oneToOne2(){

        //creamos el cliente detalle
        ClientDetails clientDetails = new ClientDetails(true, 5000);
        clientDetailsRepository.save(clientDetails);


        //creamos el cliente y le pasamos el detalle al cliente
        Client client = new Client("Svetlana", "Svet");
        client.setClientDetails(clientDetails);
        clienteRepository.save(client);


        System.out.println("cliente guadar: " + client);


    }


    /*

    //la llave foranea esta en la tabla ClientDetails
    @Transactional
    public void oneToOne(){

        //creamos el cliente y guardamos en la bd
        Client client = new Client("Svetlana", "Svet");
        clienteRepository.save(client);

        //clientDetails le ponemos los datos le pasamos el cliente le pasamos la relacion
        //donde clientDetails es el dueño de la ralacion y luego se persiste
        ClientDetails clientDetails = new ClientDetails(true, 5000);
        clientDetails.setClient(client); //pasamos al cliente el detalle
        clientDetailsRepository.save(clientDetails);

    }
     */







    //elimina una factura 2
    @Transactional
    public void removeInvoiceBidireccional2() {

           Client client = new Client("Fran", "Moras");
            //creamos dos facturas
            Invoice invoice1 = new Invoice("compras de la casa", 5000L);
            Invoice invoice2 = new Invoice("compras de oficina", 8000L);

            //al cliente le agregamos la factura
            client.addInvoice(invoice1).addInvoice(invoice2);

            //guaradamos al cliente
            clienteRepository.save(client);

            System.out.println("guardamos cliente = " + client);

        //busca cliente por el id=3
        Optional<Client> optionalClientDb = clienteRepository.findOne(3L);

        //
        optionalClientDb.ifPresent(clientDb -> {
            //Invoice invoice3 = new Invoice("compras de la casa", 5000L);
            //invoice3.setId(1L); //eliminando la factura 1
            //Optional<Invoice> invoiceOptional = Optional.of(invoice3);

            //buscamos en la base de datos la factura 2 y regresa un optional eliminamos la factura 2
            Optional<Invoice> invoiceOptional = invoiceRepository.findById(2L);
            invoiceOptional.ifPresent(invoice -> {

                // se comenta porque se pone a un metodo removeInvoice() en Client
                // client.getInvoices().remove(invoice); //se elimina del cliente la factura
                // invoice.setClient(null);  //se elimina de la factura el cliente

                clientDb.removeInvoice(invoice);
                clienteRepository.save(clientDb);
                System.out.println("cliente Actualizado:  " + clientDb);

            });

        });
    }




    //elimina una factura 1
    @Transactional
    public void removeInvoiceBidireccional() {

        Optional<Client> optionalClient  = Optional.of(new Client("Fran", "Moras"));


        //busca el cliente y le pone la factura
        optionalClient.ifPresent(client -> {

            //creamos dos facturas
            Invoice invoice1 = new Invoice("compras de la casa", 5000L);
            Invoice invoice2 = new Invoice("compras de oficina", 8000L);

            client.addInvoice(invoice1).addInvoice(invoice2);

            clienteRepository.save(client);

            System.out.println("client = " + client);
        });

        //busca cliente y elimina una factura
        Optional<Client> optionalClientDb = clienteRepository.findOne(1L);

        optionalClientDb.ifPresent(client -> {
            Invoice invoice3 = new Invoice("compras de la casa", 5000L);
            invoice3.setId(1L); //eliminando la factura 1

            //buscamos en la base de datos la factura y regresa un optional eliminamos la factura 3
            Optional<Invoice> invoiceOptional = Optional.of(invoice3);

            invoiceOptional.ifPresent(invoice -> {

                // se comenta porque se pone a un metodo removeInvoice() en Client
                //  client.getInvoices().remove(invoice); //se elimina del cliente la factura
                // invoice.setClient(null);  //se elimina de la factura el cliente

                client.removeInvoice(invoice);
                clienteRepository.save(client);
                System.out.println("client = " + client);

            });

        });
    }






    //elimina una factura de la oficina
    @Transactional
    public void removeInvoiceBidireccionalFindById2() {

        //busca cliente
        Optional<Client> optionalClient = clienteRepository.findOne(1L);

        //busca el cliente y le pone la factura
        optionalClient.ifPresent(client -> {

            //creamos dos facturas
            Invoice invoice1 = new Invoice("compras de la casa", 5000L);
            Invoice invoice2 = new Invoice("compras de oficina", 8000L);

            client.addInvoice(invoice1).addInvoice(invoice2);

            clienteRepository.save(client);

            System.out.println("client = " + client);
        });

        //busca cliente y elimina una factura
        Optional<Client> optionalClientDb = clienteRepository.findOne(1L);

        optionalClientDb.ifPresent(client -> {
            Invoice invoice3 = new Invoice("compras de la casa", 5000L);
            invoice3.setId(1L); //eliminando la factura 1

            //buscamos en la base de datos la factura y regresa un optional eliminamos la factura 3
            Optional<Invoice> invoiceOptional = Optional.of(invoice3);

            invoiceOptional.ifPresent(invoice -> {

                // se comenta porque se pone a un metodo removeInvoice() en Client
                //  client.getInvoices().remove(invoice); //se elimina del cliente la factura
                // invoice.setClient(null);  //se elimina de la factura el cliente

                client.removeInvoice(invoice);
                clienteRepository.save(client);
                System.out.println("client = " + client);

            });

        });
    }







    @Transactional
    public void oneToManyInvoiceBidireccionalFindById() {

        //Optional<Client> optionalClient = clienteRepository.findById(1L);

        //Optional<Client> optionalClient = clienteRepository.findOneWithInvoices(1L);

        //trae el clinet con sus facturas y direcciones
        Optional<Client> optionalClient = clienteRepository.findOne(1L);
          //el cliente viene de la bd
          optionalClient.ifPresent(client -> {

              //creamos dos facturas
              Invoice invoice1 = new Invoice("compras de la casa", 5000L);
              Invoice invoice2 = new Invoice("compras de oficina", 8000L);

              client.addInvoice(invoice1).addInvoice(invoice2);

              clienteRepository.save(client);

              System.out.println("client = " + client);
          });

    }



//optimizado
//asignado las facturas al cliente
    @Transactional
    public void oneToManyInvoiceBidireccional() {
        Client client = new Client("Fran", "Moras");

        //creamos dos facturas
        Invoice invoice1 = new Invoice("compras de la casa", 5000L);
        Invoice invoice2 = new Invoice("compras de oficina", 8000L);

        // se comenta porque se pone enforma encadenada
        // client.addInvoice(invoice1);
        // client.addInvoice(invoice2);

        //se comenta porque se pone enforma encadenada
        client.addInvoice(invoice1)
                .addInvoice(invoice2);


        clienteRepository.save(client);

        System.out.println("client = " + client);

    }



/*
    //asignado las facturas al cliente
    @Transactional
    public void oneToManyInvoiceBidireccional() {
        Client client = new Client("Fran", "Moras");

        //creamos dos facturas
      Invoice invoice1 = new Invoice("compras de la casa", 5000L);
      Invoice invoice2 = new Invoice("compras de oficina", 8000L);

      //asignamos al cliente las facturas en una lista
      //el cliente tiene muchas facturas  @OneToOne
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(invoice1);
        invoices.add(invoice2);
        client.setInvoices(invoices);

         //en facturas le pasamos al cliente porque es un operacion bidireccional inversa
        //a la factura le pasamos al cliente
        invoice1.setClient(client);
        invoice2.setClient(client);

        clienteRepository.save(client);

        System.out.println("client = " + client);

    }
*/




    //elimina la direccion1
    @Transactional
    public void manyToOne(){

        //crea un cliente
        Client client = new Client("Andrea", "Andrea");
        clienteRepository.save(client);

        //asignamos a la factura
        Invoice invoice= new Invoice("compras de oficina", 2000L);
        invoice.setClient(client);
        Invoice invoiceDB = invoiceRepository.save(invoice);
        System.out.println("invoiceDB = " + invoiceDB);

    }


    @Transactional
    public void manyToOneFindById(){
        //busca un cliente en la base de datos regresa un optional
        Optional<Client> optionalClient = clienteRepository.findById(1L);
        if(optionalClient.isPresent()){
            Client client = optionalClient.orElseThrow();

            //asignamos a la factura al cliente que encontramos en la base de datos
            Invoice invoice= new Invoice("compras de oficina", 2000L);
            invoice.setClient(client);
            Invoice invoiceDB = invoiceRepository.save(invoice);
            System.out.println("invoiceDB = " + invoiceDB);

        }

    }


    @Transactional
    public void oneToMany() {
        Client client = new Client("Fran", "Moras");

        Address address1 = new Address("El vergel", 1234);
        Address address2 = new Address("Arboleda",890);

        //agregamos las direcciones al cliente

        client.getAddresses().add(address1);
        client.getAddresses().add(address2);

        clienteRepository.save(client);
        System.out.println("client = " + client);

    }


    @Transactional
    public void oneToManyFindById() {
        Optional<Client> optionalClient = clienteRepository.findById(2L);

         optionalClient.ifPresent(client -> {

             Address address1 = new Address("El vergel", 1234);
             Address address2 = new Address("Arboleda",890);

             Set<Address> addresses = new HashSet<>();
             addresses.add(address1);
             addresses.add(address2);

             client.setAddresses(addresses);


             //agregamos las direcciones al cliente

            //client.setAddresses(Arrays.asList(address1, address2));

             clienteRepository.save(client);
             System.out.println("client = " + client);
             });

         }


    @Transactional
    public void removeAddress() {

          //buscamos al cliente en la base de datos
        Client client = new Client("Fran", "Moras");


        Address address1 = new Address("El vergel", 1234);
        Address address2 = new Address("Arboleda",890);

        //agregamos las direcciones al cliente

        client.getAddresses().add(address1);
        client.getAddresses().add(address2);

        clienteRepository.save(client);
        System.out.println("client = " + client);


        //buscamos al cliente
        //hace una consulta para obtener el cliente por el id
        Optional<Client> optionalClient = clienteRepository.findById(3L);

        //hace otra consulta para obtener sus direcciones
        optionalClient.ifPresent(c -> {
          //obtiene la direccion del cliente
            c.getAddresses().remove(address1);

            /*
                hace un fecth una consulta tardia Lazy por las dos concultas que hace sale error
                not initializar proxy - no session
                para solucionarlo ponemos en el properties
                la etiqueta
                spring.jpa.properties.hibernate.enable_lazy_load_no_trans= true
                con lazy solo hace la consulta cuando llama al metodo get
                porque cierra la conexion en el primera consulta y despues al hacer la segunda consulta no puede hacer la
                segunda consulta para poder eliminar la direccion

             */



            //update en el cliente
            //modificando el cliente para actualizarla para que elimine la direccion que esta en el cliente
             clienteRepository.save(c);
             System.out.println("cliente actualizado con la direccion  = " + c);

        });

    }


    //elimina la direccion2
    //para que no de error
    //utilizamos el query para que obtener las dos direcciones en una sola consulta
    //    @Query("select c from Client c join fetch c.addresses")
    //    Optional<Client> findOne(Long id);
    // ponemos en el properties
    // spring.jpa.properties.hibernate.enable_lazy_load_no_trans= true
    // o ponemos en el atributo de la clase entity fetch = FetchType.EAGER
    //@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Transactional
    public void removeAddressFindById() {
        Optional<Client> optionalClient = clienteRepository.findById(2L);
        optionalClient.ifPresent(client -> {
            Address address1 = new Address("El vergel", 1234);
            Address address2 = new Address("Arboleda",890);

            Set<Address> addresses = new HashSet<>();
            addresses.add(address1);
            addresses.add(address2);
            client.setAddresses(addresses);

            //client.setAddresses(Arrays.asList(address1, address2));

            clienteRepository.save(client);

            System.out.println("client = " + client);

            Optional<Client> optionalClient2 = clienteRepository.findById(2L);
           // Optional<Client> optionalClient2 = clienteRepository.findOne(2L);
            optionalClient2.ifPresent(c -> {
                c.getAddresses().remove(address2);
                clienteRepository.save(c);
                System.out.println("cliente actualizado con la direccion2 = " + c);

            });

        });
    }

}
