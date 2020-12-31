package br.com.itarocha.betesda.application.port.in;

import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.TipoLeito;

import java.util.List;

public interface TipoLeitoUseCase {

	TipoLeito create(TipoLeito model);

	void remove(Long id);

	TipoLeito update(TipoLeito model);

	TipoLeito find(Long id);

	List<TipoLeito> findAll();

	List<SelectValueVO> listSelect();

}
