package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class EshopApplicationMainTest {

  @Test
  void main_invokesSpringApplicationRun() {
    try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
      EshopApplication.main(new String[] {});

      mockedSpringApplication.verify(() -> SpringApplication.run(EshopApplication.class, new String[] {}));
    }
  }
}
