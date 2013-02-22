#-*- coding: utf-8 -*-

require "natto"

module NukokusaBot

  class Natto::MeCabNode
    def compress
      self.feature[0, 2] + self.surface
    end
  end

  class MarkovChain

    def initialize
      @word_id  = Hash.new  # {id1 => 'word1', id2 => 'word2'}
      @tailings = Hash.new  # {id1 => {id2 => count, id3 => count}}
      @mecab = Natto::MeCab.new

      @max_id  = -1
      register_words(['末尾', '先頭'])
    end

    def add(sentence)
      words = @mecab.parse_as_nodes(sentence)[0..-2].map(&:compress)

      register_words(words)

      count_tailings('先頭', words.first)
      words.each_cons(2){ |pre, suc| count_tailings(pre, suc) }
      count_tailings(words.last, '末尾')

      require "pp"
      pp @tailings
    end
    alias_method :<<, :add
    alias_method :append, :add

    def generate(limit = 140)
      puts
      chain_ids = [rand(2..@max_id)]

      chain_ids << choose_next_of(chain_ids.last) until chain_ids.last == 0

      chain_to_string(chain_ids)
    end

    private

    def choose_next_of(id)
      candidates = Array.new
      @tailings[id].each do |id, count|
        candidates << [id] * count
      end
      candidates.flatten.sample
    end

    def chain_to_string(ids)
      puts @word_id.invert.values_at(*ids).collect{|word| word[2..-1]}.join
    end

    def register_words(words)
      words.each do |word|
        unless @word_id.has_key? word
          @max_id += 1
          @word_id[word] = @max_id
        end
      end
    end

    def count_tailings(pre, suc)
      pre_id = @word_id[pre]
      suc_id = @word_id[suc]

      if @tailings.has_key? pre_id
        if @tailings[pre_id].has_key? suc_id
          @tailings[pre_id][suc_id] += 1
        else
          @tailings[pre_id][suc_id] = 1
        end
      else
        @tailings[pre_id] = Hash.new
        @tailings[pre_id][suc_id] = 1
      end
    end
  end
end
