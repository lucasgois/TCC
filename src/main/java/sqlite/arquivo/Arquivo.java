package sqlite.arquivo;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Arquivo {
    private String hash;
    private byte[] bytea;
}