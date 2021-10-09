package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.TipoLeito;

import java.util.List;

public interface TipoLeitoUseCase {
	TipoLeito create(TipoLeito model);
	void remove(Long id);
	TipoLeito update(TipoLeito model);
	TipoLeito find(Long id);
	List<TipoLeito> findAll();
	List<ItemDictionary> listSelect();
}
