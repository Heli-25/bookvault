package com.bookvault.bookvault.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BookDTO {
    private String id;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "978-\\d{10}", message = "Invalid ISBN format")
    private String isbn;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private String genre;

    @Min(1)
    private int totalCopies;

    private int availableCopies;

}
