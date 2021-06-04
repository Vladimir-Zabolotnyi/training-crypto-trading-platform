package sigma.training.ctp.mapper;


public interface Mapper<E, D> {

  D toRestDto(E entity);

  E toEntity(D restDto);
}
